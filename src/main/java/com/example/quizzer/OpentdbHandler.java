package com.example.quizzer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class OpentdbHandler implements JSONProvider{

    private static final URI questionsURI = URI.create("https://opentdb.com/api.php?amount=5");

    public JsonNode getJSON() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        HttpClient httpClient = HttpClient.newHttpClient();;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(questionsURI)
                .GET()
                .build();

        try{
            do {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                int statusCode = response.statusCode();

                if (statusCode != 200) {
                    if (statusCode == 429) {
                        TimeUnit.SECONDS.sleep(6);
                    } else {
                        throw new OpentdbException();
                    }
                } else {
                    JsonNode jsonNode = mapper.readTree(response.body());
                    if (jsonNode.get("response_code").asInt() != 0)
                        throw new OpentdbException();

                    return jsonNode.get("results");
                }
            }while(true);
        } catch (Exception e){
            throw new OpentdbException(e);
        }
    }
}
