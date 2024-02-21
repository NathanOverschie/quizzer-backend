package com.example.quizzer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

public class OpentdbHandler implements JSONProvider{

    // url to the opentdb endpoint
    private static final URI questionsURI = URI.create("https://opentdb.com/api.php?amount=5");

    public JsonNode getJSON() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(questionsURI)
                .GET()
                .build();

        try{
            do {
                // get response
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                int statusCode = response.statusCode();

                if (statusCode != 200) {
                    // not OK
                    if (statusCode == 429) {
                        // too many requests
                        // sleep to let the server cool down
                        // afterward try again
                        TimeUnit.SECONDS.sleep(6);
                    } else {
                        // problem with the server
                        throw new OpentdbException();
                    }
                } else {
                    // OK
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
