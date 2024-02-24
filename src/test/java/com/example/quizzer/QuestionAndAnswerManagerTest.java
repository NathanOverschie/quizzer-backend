package com.example.quizzer;

import com.example.quizzer.dataproviders.IQuestionProvider;
import com.example.quizzer.utils.CorrectAnswerNotFoundException;
import com.example.quizzer.utils.NotEnoughQuestionsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class QuestionAndAnswerManagerTest {

    private QuestionAndAnswerManager questionAndAnswerManager;
    private fakeQuestionProvider questionProvider;

    private static final int maxAmountPerRequest = 50;

    static class fakeQuestionProvider implements IQuestionProvider {
        private int counter = 0;
        public int requestsMade = 0;

        public String getCorrectAnswer(QuestionWithPossibleAnswers questionWithPossibleAnswers){
            return questionWithPossibleAnswers.possibleAnswers()
                    .stream()
                    .filter(x -> x.startsWith("answer"))
                    .findFirst()
                    .get();
        }

        public String getIncorrectAnswer(QuestionWithPossibleAnswers questionWithPossibleAnswers){
            return questionWithPossibleAnswers.possibleAnswers()
                    .stream()
                    .filter(x -> x.startsWith("falseAnswer"))
                    .findFirst()
                    .get();
        }

        private QuestionWithAnswerAndFalseAnswers getQuestionWithAnswerAndFalseAnswers() {
            counter++;

            ArrayList<String> falseAnswers = new ArrayList<>();
            falseAnswers.add("falseAnswers" + counter  + "1");
            falseAnswers.add("falseAnswers" + counter  + "2");
            falseAnswers.add("falseAnswers" + counter  + "3");

            return new QuestionWithAnswerAndFalseAnswers("question" + counter, "answer" + counter, falseAnswers);
        }

        @Override
        public List<QuestionWithAnswerAndFalseAnswers> getMaxQuestionsWithAnswerAndFalseAnswers() {
            ArrayList<QuestionWithAnswerAndFalseAnswers> questionWithAnswerAndFalseAnswers = new ArrayList<>();

            requestsMade++;

            for(int i = 0; i < maxAmountPerRequest ; i ++){
                questionWithAnswerAndFalseAnswers.add(getQuestionWithAnswerAndFalseAnswers());
            }

            return questionWithAnswerAndFalseAnswers;
        }
    }

    @BeforeEach
    void init(){
        questionProvider = new fakeQuestionProvider();
        questionAndAnswerManager = new QuestionAndAnswerManager(questionProvider, 10_000);
    }

    boolean validQuestionAndAnswer(QuestionWithPossibleAnswers questionWithPossibleAnswers){
        return questionWithPossibleAnswers.text() != null &&
                questionWithPossibleAnswers.possibleAnswers() != null &&
                questionWithPossibleAnswers.possibleAnswers()
                        .stream()
                        .allMatch(Objects::nonNull);
    }

    @Test
    void getQuestionsWithPossibleAnswersFive(){
        try {
            //Setup
            List<QuestionWithPossibleAnswers> questionsWithPossibleAnswers = questionAndAnswerManager.getQuestionsWithPossibleAnswers(5);

            //Act & Assert
            assertEquals(5, questionsWithPossibleAnswers.size());
            assertTrue(
                    questionsWithPossibleAnswers
                            .stream()
                            .allMatch(this::validQuestionAndAnswer));

        } catch (NotEnoughQuestionsException e) {
            fail();
        }
    }

    @Test
    void getQuestionsWithPossibleAnswersHundred(){
        try {
            //Setup
            List<QuestionWithPossibleAnswers> questionsWithPossibleAnswers = questionAndAnswerManager.getQuestionsWithPossibleAnswers(100);

            //Act & Assert
            assertEquals(100, questionsWithPossibleAnswers.size());
            assertTrue(
                    questionsWithPossibleAnswers
                            .stream()
                            .allMatch(this::validQuestionAndAnswer));

        } catch (NotEnoughQuestionsException e) {
            fail();
        }
    }

    @Test
    void getQuestionsWithPossibleAnswersNoUnnecessaryRequests(){
        try{
            //Act
            for (int i = 0; i < maxAmountPerRequest; i++){
                questionAndAnswerManager.getQuestionsWithPossibleAnswers(1);
            }

            //Assert
            assertEquals(questionProvider.requestsMade, 1);
        } catch (NotEnoughQuestionsException e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void checkAnswerCorrectAnswer(){
        try {
            // Setup
            List<QuestionWithPossibleAnswers> questionsWithPossibleAnswers =
                    questionAndAnswerManager.getQuestionsWithPossibleAnswers(5);
            QuestionWithPossibleAnswers questionWithPossibleAnswers =
                    questionsWithPossibleAnswers.getFirst();

            int ID = questionWithPossibleAnswers.ID();
            String correct = questionProvider.getCorrectAnswer(questionWithPossibleAnswers);

            //Act
            boolean result = questionAndAnswerManager.checkAnswer(ID, correct);

            //Assert
            assertTrue(result);

        } catch (NotEnoughQuestionsException | CorrectAnswerNotFoundException e) {
            fail();
        }
    }

    @Test
    void checkAnswerIncorrectAnswer(){
        try {
            // Setup
            List<QuestionWithPossibleAnswers> questionsWithPossibleAnswers =
                    questionAndAnswerManager.getQuestionsWithPossibleAnswers(5);
            QuestionWithPossibleAnswers questionWithPossibleAnswers =
                    questionsWithPossibleAnswers.getFirst();

            int ID = questionWithPossibleAnswers.ID();
            String correct = questionProvider.getIncorrectAnswer(questionWithPossibleAnswers);

            //Act
            boolean result = questionAndAnswerManager.checkAnswer(ID, correct);

            //Assert
            assertFalse(result);

        } catch (NotEnoughQuestionsException | CorrectAnswerNotFoundException e) {
            fail();
        }
    }
}