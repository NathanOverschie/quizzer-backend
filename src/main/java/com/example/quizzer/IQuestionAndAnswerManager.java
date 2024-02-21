package com.example.quizzer;

import java.util.List;

public interface IQuestionAndAnswerManager {

    List<QuestionWithPossibleAnswers> getQuestionsWithPossibleAnswers(int amount) throws NotEnoughQuestionsException;

    boolean checkAnswer(int ID, String Answer);
}
