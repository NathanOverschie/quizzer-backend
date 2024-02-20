package com.example.quizzer;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
public class OpentdbHandler {

    static final URL questionsURL;

    static {
        try {
            questionsURL = new URI("https://opentdb.com/api.php?amount=10").toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    OpentdbHandler() {
    }

    public List<QuestionWithAnswer> getQuestions() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        HttpsURLConnection connection = (HttpsURLConnection) questionsURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        System.out.println(connection);

        return new ArrayList<>();
    }
}
