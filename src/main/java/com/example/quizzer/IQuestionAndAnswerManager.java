package com.example.quizzer;

import com.example.quizzer.utils.CorrectAnswerNotFoundException;
import com.example.quizzer.utils.NotEnoughQuestionsException;

import java.util.List;

public interface IQuestionAndAnswerManager {

    List<QuestionWithPossibleAnswers> getQuestionsWithPossibleAnswers(int amount) throws NotEnoughQuestionsException;

    boolean checkAnswer(int ID, String Answer) throws CorrectAnswerNotFoundException;
}
