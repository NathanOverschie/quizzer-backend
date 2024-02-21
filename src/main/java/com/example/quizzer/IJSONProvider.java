package com.example.quizzer;

import com.fasterxml.jackson.databind.JsonNode;
public interface IJSONProvider {

    JsonNode getJSON() throws Exception;
}
