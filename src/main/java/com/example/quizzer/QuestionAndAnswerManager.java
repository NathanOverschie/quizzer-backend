package com.example.quizzer;

import java.util.*;

public class QuestionAndAnswerManager implements IQuestionAndAnswerManager {
    private final IQuestionProvider questionProvider;
    private int counter = 0;

    private List<QuestionWithAnswerAndFalseAnswers> questionStack = new LinkedList<>();

    private final Map<Integer, String> correctAnswers = new HashMap<>();

    QuestionAndAnswerManager(IQuestionProvider questionProvider){
        this.questionProvider = questionProvider;
    }

    private int newId(){
        return counter++;
    }

    private QuestionWithPossibleAnswers HideAnswer(QuestionWithAnswerAndFalseAnswers questionWithAnswerAndFalseAnswers) {
        List<String> possibleAnswers = new ArrayList<>(questionWithAnswerAndFalseAnswers.falseAnswers());
        possibleAnswers.add(questionWithAnswerAndFalseAnswers.answer());
        Collections.shuffle(possibleAnswers);

        return new QuestionWithPossibleAnswers(
                        newId(),
                        questionWithAnswerAndFalseAnswers.question(),
                        possibleAnswers);
    }

    @Override
    public List<QuestionWithPossibleAnswers> getQuestionsWithPossibleAnswers(int amount) throws NotEnoughQuestionsException {
        if(questionStack.size() < amount){
            try {
                questionStack.addAll(questionProvider.getMaxQuestionsWithAnswerAndFalseAnswers());
            } catch (Exception e) {
                throw new NotEnoughQuestionsException();
            }
        }

        List<QuestionWithAnswerAndFalseAnswers> questionsWithAnswerAndFalseAnswers = questionStack.subList(0, amount);
        questionStack = questionStack.subList(amount, questionStack.size());

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
