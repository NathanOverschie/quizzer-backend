package com.example.quizzer;

import java.util.List;

;

public interface QuestionProvider {
    List<QuestionWithAnswerAndFalseAnswers> getQuestionWithAnswerAndFalseAnswers() throws Exception;
}
