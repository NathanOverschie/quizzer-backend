package com.example.quizzer;

import java.util.List;
public record QuestionWithAnswerAndFalseAnswers (String question, String answer, List<String> falseAnswers) {}
