package com.example.quizzer;

import java.util.List;
public record Question(Integer Id, String text, List<String> answers){
}
