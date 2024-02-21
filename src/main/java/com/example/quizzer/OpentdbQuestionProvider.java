package com.example.quizzer;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class OpentdbQuestionProvider implements QuestionProvider{
    private final JSONProvider jsonProvider;

    OpentdbQuestionProvider(JSONProvider jsonProvider){
        this.jsonProvider = jsonProvider;
    }

    private QuestionWithAnswerAndFalseAnswers POJOFromJSONQuestion(JsonNode jsonNode){
        String text = jsonNode.get("question").asText();
        String answer = jsonNode.get("correct_answer").asText();
        List<String> falseAnswers =
                StreamSupport.stream(jsonNode.get("incorrect_answers").spliterator(), false)
                .map(JsonNode::asText)
                .toList();

        return new QuestionWithAnswerAndFalseAnswers(
                text,
                answer,
                falseAnswers);
    }

    @Override
    public List<QuestionWithAnswerAndFalseAnswers> getQuestionWithAnswerAndFalseAnswers() throws Exception {
        JsonNode questionsJSON = jsonProvider.getJSON();

        List<QuestionWithAnswerAndFalseAnswers> result = new ArrayList<>();

        if(questionsJSON.isArray()){
            for (JsonNode questionJSON : questionsJSON) {
                result.add(POJOFromJSONQuestion(questionJSON));
            }
        }else{
            result.add(POJOFromJSONQuestion(questionsJSON));
        }

        return result;
    }
}
