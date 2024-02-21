package com.example.quizzer;

import java.util.List;

public record QuestionWithPossibleAnswers (int ID, String text, List<String> possibleAnswers) {}
