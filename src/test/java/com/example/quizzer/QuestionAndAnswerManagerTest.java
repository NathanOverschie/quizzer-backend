package com.example.quizzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.internal.matchers.Not;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class QuestionAndAnswerManagerTest {

    private QuestionAndAnswerManager questionAndAnswerManager;
    private fakeQuestionProvider questionProvider;

    class fakeQuestionProvider implements IQuestionProvider{
        private int counter = 0;

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
        public List<QuestionWithAnswerAndFalseAnswers> getQuestionsWithAnswerAndFalseAnswers() throws Exception {
            ArrayList<QuestionWithAnswerAndFalseAnswers> questionWithAnswerAndFalseAnswers = new ArrayList<>();

            for(int i = 0; i < 10 ; i ++){
                questionWithAnswerAndFalseAnswers.add(getQuestionWithAnswerAndFalseAnswers());
            }

            return questionWithAnswerAndFalseAnswers;
        }
    }

    @BeforeEach
    void init(){
        questionProvider = new fakeQuestionProvider();
        questionAndAnswerManager = new QuestionAndAnswerManager(questionProvider);
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
            List<QuestionWithPossibleAnswers> questionsWithPossibleAnswers = questionAndAnswerManager.getQuestionsWithPossibleAnswers(5);

            assertTrue(
                    questionsWithPossibleAnswers
                            .stream()
                            .allMatch(this::validQuestionAndAnswer));

        } catch (NotEnoughQuestionsException e) {
            fail();
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

        } catch (NotEnoughQuestionsException e) {
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

        } catch (NotEnoughQuestionsException e) {
            fail();
        }
    }
}