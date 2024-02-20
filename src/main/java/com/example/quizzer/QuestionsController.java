package com.example.quizzer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionsController {

    @GetMapping("/questions")
    public List<Question> Questions(){
        return new ArrayList<>();
    }
}
