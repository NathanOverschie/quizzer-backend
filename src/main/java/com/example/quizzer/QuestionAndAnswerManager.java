package com.example.quizzer;

import java.util.List;
import java.util.Stack;

;

public class QuestionAndAnswerManager implements IQuestionAndAnswerManager {
    private final IQuestionProvider questionProvider;

    private int counter = 0;

    private Stack<QuestionWithAnswerAndFalseAnswers> questionStack;
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
        try {
            return questionProvider.getQuestionsWithAnswerAndFalseAnswers()
                    .stream()
                    .map(this::HideAnswer)
                    .toList();
        } catch (Exception e) {
            throw new NotEnoughQuestionsException();
        }
    }

    @Override
    public boolean checkAnswer(int ID, String Answer) {
        return false;
    }
}
