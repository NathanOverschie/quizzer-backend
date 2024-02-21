package com.example.quizzer;

import java.util.*;

public class QuestionAndAnswerManager implements IQuestionAndAnswerManager {
    private final IQuestionProvider questionProvider;

    private int counter = 0;

    Map<Integer, String> correctAnswers = new HashMap<>();

    QuestionAndAnswerManager(IQuestionProvider questionProvider){
        this.questionProvider = questionProvider;
    }

    private int newId(){
        return counter++;
    }

    private QuestionWithPossibleAnswers HideAnswer(QuestionWithAnswerAndFalseAnswers questionWithAnswerAndFalseAnswers) {
        List<String> possibleAnswers = questionWithAnswerAndFalseAnswers.falseAnswers();
        possibleAnswers.add(questionWithAnswerAndFalseAnswers.answer());

        return new QuestionWithPossibleAnswers(
                        newId(),
                        questionWithAnswerAndFalseAnswers.question(),
                        possibleAnswers);
    }

    @Override
    public List<QuestionWithPossibleAnswers> getQuestionsWithPossibleAnswers(int amount) throws NotEnoughQuestionsException {
        List<QuestionWithAnswerAndFalseAnswers> questionsWithAnswerAndFalseAnswers;
        try {
            questionsWithAnswerAndFalseAnswers = questionProvider.getQuestionsWithAnswerAndFalseAnswers();
        } catch (Exception e) {
            throw new NotEnoughQuestionsException();
        }

        List<QuestionWithPossibleAnswers> result = new ArrayList<>();

        for (QuestionWithAnswerAndFalseAnswers questionsWithAnswerAndFalseAnswer : questionsWithAnswerAndFalseAnswers) {
            QuestionWithPossibleAnswers questionWithPossibleAnswers = HideAnswer(questionsWithAnswerAndFalseAnswer);
            result.add(questionWithPossibleAnswers);
            correctAnswers.put(questionWithPossibleAnswers.ID(), questionsWithAnswerAndFalseAnswer.answer());
        }

        return result;
    }

    @Override
    public boolean checkAnswer(int ID, String Answer) throws CorrectAnswerNotFoundException {
        String correctAnswer = correctAnswers.get(ID);

        if(correctAnswer == null){
            throw new CorrectAnswerNotFoundException();
        }

        return Answer.equals(correctAnswer);
    }
}
