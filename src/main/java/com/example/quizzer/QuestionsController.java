package com.example.quizzer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionsController {
    private final QuestionAndAnswerManager manager = new QuestionAndAnswerManager(new OpentdbQuestionProvider(new OpentdbHandler()));

    @GetMapping("/questions")
    public List<QuestionWithPossibleAnswers> Questions(){
        try {
            return manager.getQuestionsWithPossibleAnswers(5);
        } catch (NotEnoughQuestionsException e) {
            throw new RuntimeException(e);
        }
    }
}
