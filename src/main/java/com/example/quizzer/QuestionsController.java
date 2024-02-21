package com.example.quizzer;

import com.example.quizzer.dataproviders.OpentdbHandler;
import com.example.quizzer.dataproviders.OpentdbQuestionProvider;
import com.example.quizzer.utils.CorrectAnswerNotFoundException;
import com.example.quizzer.utils.NotEnoughQuestionsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionsController {
    private final QuestionAndAnswerManager manager =
            new QuestionAndAnswerManager(
                    new OpentdbQuestionProvider(
                            new OpentdbHandler()),
                    1000);

    @GetMapping("/questions")
    public ResponseEntity<?> Questions(){
        try {
            return ResponseEntity.ok(manager.getQuestionsWithPossibleAnswers(5));
        } catch (NotEnoughQuestionsException e) {
            return ResponseEntity.badRequest().body("Not enough questions");
        }
    }

    @PostMapping("/checkanswers")
    public ResponseEntity<?> CheckAnswers(
            @RequestParam int ID,
            @RequestParam String answer
    ) {
        try {
            return ResponseEntity.ok(manager.checkAnswer(ID, answer));
        } catch (CorrectAnswerNotFoundException e) {
            return ResponseEntity.badRequest().body("Correct answer was not found");
        }
    }
}
