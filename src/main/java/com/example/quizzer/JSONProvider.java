package com.example.quizzer;

import com.fasterxml.jackson.databind.JsonNode;
public interface JSONProvider {
    JsonNode getJSON() throws Exception;
}
