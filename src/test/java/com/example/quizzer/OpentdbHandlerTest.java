package com.example.quizzer;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


class OpentdbHandlerTest {
    private JSONProvider opentdbHandler;

    @BeforeEach
    void init(){
        opentdbHandler = new OpentdbHandler();
    }

    @Test
    void getQuestionsDoesNotThrow() {
        assertDoesNotThrow(() -> {
            opentdbHandler.getJSON();
        });
    }

    @Test
    void getQuestionReturnsFiveOnDefault(){
        try {
            JsonNode questionsJSON = opentdbHandler.getJSON();

            assertTrue(questionsJSON.isArray());
            assertEquals(questionsJSON.size(), 5);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void getQuestionNotNull(){
        try {
            JsonNode questions = opentdbHandler.getJSON();
            assertNotNull(questions);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void getQuestionsMultipleRequestsNotNull(){
        try {
            for(int i = 0; i < 5; i++) {
                assertNotNull(opentdbHandler.getJSON());
                TimeUnit.MILLISECONDS.sleep(5100);
            }
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }
}