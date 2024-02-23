package com.example.quizzer;

import com.example.quizzer.dataproviders.OpentdbHandler;
import com.example.quizzer.dataproviders.OpentdbQuestionProvider;
import com.example.quizzer.utils.CorrectAnswerNotFoundException;
import com.example.quizzer.utils.NotEnoughQuestionsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionsController {
    private final QuestionAndAnswerManager manager =
            new QuestionAndAnswerManager(
                    new OpentdbQuestionProvider(
                            new OpentdbHandler()),
                    1000);

    @GetMapping(value = "/questions", produces = "application/json")
    public ResponseEntity<?> Questions(){
        try {
            return ResponseEntity.ok(manager.getQuestionsWithPossibleAnswers(5));
        } catch (NotEnoughQuestionsException e) {
            return ResponseEntity.badRequest().body("Not enough questions");
        }
    }

    @PostMapping(value = "/checkanswers", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> CheckAnswers(
                @RequestBody String body
            ) {
        ObjectMapper mapper = new ObjectMapper();

        try{
            JsonNode jsonNode = mapper.readTree(body);

            int ID = jsonNode.get("ID").asInt();
            String answer = jsonNode.get("answer").asText();

            return ResponseEntity.ok(manager.checkAnswer(ID, answer));

        } catch (JsonProcessingException | NullPointerException e) {
            return ResponseEntity.badRequest().body("malformed json");
        } catch (CorrectAnswerNotFoundException e) {
            return ResponseEntity.badRequest().body("Correct answer was not found");
        }
    }
}
