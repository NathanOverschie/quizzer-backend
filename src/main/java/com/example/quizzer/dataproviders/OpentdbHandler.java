package com.example.quizzer.dataproviders;

import com.example.quizzer.dataproviders.IQuestionsJSONProvider;
import com.example.quizzer.utils.OpentdbException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class OpentdbHandler implements IQuestionsJSONProvider {

    private static final String url = "https://opentdb.com/api.php";
    private static Instant lastUsed = null;
    private static final int delayBetweenRequestsInMillis = 5000;
    private static final int maxAmount = 50;

    public JsonNode getJSON(int amount) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?amount=" + amount))
                .GET()
                .build();

        Instant now = Instant.now();

        // wait until the endpoint is available
        if(lastUsed != null) {
            Instant available = lastUsed.plus(delayBetweenRequestsInMillis, ChronoUnit.MILLIS);
            if (available.isAfter(now)) {
                TimeUnit.MILLISECONDS.sleep(now.until(available, ChronoUnit.MILLIS));
            }
        }

        try {
            // get response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            lastUsed = Instant.now();

            JsonNode jsonNode = mapper.readTree(response.body());
            if (jsonNode.get("response_code").asInt() != 0)
                throw new OpentdbException();

            return jsonNode.get("results");

        } catch (Exception e) {
            throw new OpentdbException(e);
        }
    }

    @Override
    public JsonNode getMaxJSON() throws Exception {
        return getJSON(maxAmount);
    }
}
