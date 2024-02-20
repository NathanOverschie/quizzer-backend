package com.example.quizzer;

import java.util.List;

public record OpentdbResponse(String category, String type, String difficulty, String question, String correct_answer, List<String> incorrect_answers) {}
