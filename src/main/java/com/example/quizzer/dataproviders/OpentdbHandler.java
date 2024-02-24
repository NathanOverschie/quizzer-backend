package com.example.quizzer.dataproviders;

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

/**
 * Handles the retrieval of JSON data from the Open Trivia Database API.
 * Implements the IQuestionsJSONProvider interface.
 */
public class OpentdbHandler implements IQuestionsJSONProvider {

    /**
     * Base URL for the Open Trivia Database API.
     */
    private static final String url = "https://opentdb.com/api.php";

    /**
     * Timestamp of the last request sent to the API.
     */
    private static Instant lastUsed = null;

    /**
     * Delay between consecutive requests to the API in milliseconds.
     */
    private static final int delayBetweenRequestsInMillis = 5000;

    /**
     * Maximum amount of questions to retrieve from the API per request.
     */
    private static final int maxAmount = 50;

    /**
     * Retrieves JSON data containing trivia questions from the Open Trivia Database API.
     *
     * @param amount Number of questions to retrieve.
     * @return JSON data containing trivia questions.
     * @throws OpentdbException If an error occurs during the retrieval process.
     */
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

    /**
     * Retrieves JSON data containing the maximum number of trivia questions from the Open Trivia Database API.
     *
     * @return JSON data containing trivia questions.
     * @throws OpentdbException If an error occurs during the retrieval process.
     */
    @Override
    public JsonNode getMaxJSON() throws Exception {
        return getJSON(maxAmount);
    }
}
