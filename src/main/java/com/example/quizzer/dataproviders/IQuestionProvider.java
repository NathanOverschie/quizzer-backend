package com.example.quizzer.dataproviders;

import com.example.quizzer.QuestionWithAnswerAndFalseAnswers;

import java.util.List;

public interface IQuestionProvider {
    List<QuestionWithAnswerAndFalseAnswers> getMaxQuestionsWithAnswerAndFalseAnswers() throws Exception;
}
