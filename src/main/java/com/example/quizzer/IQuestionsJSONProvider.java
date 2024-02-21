package com.example.quizzer;

import com.fasterxml.jackson.databind.JsonNode;
public interface IQuestionsJSONProvider {
    JsonNode getJSON(int amount) throws Exception;

    JsonNode getMaxJSON() throws Exception;
}
