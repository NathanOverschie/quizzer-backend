package com.example.quizzer;

import java.util.List;

public interface IQuestionProvider {
    List<QuestionWithAnswerAndFalseAnswers> getMaxQuestionsWithAnswerAndFalseAnswers() throws Exception;
}
