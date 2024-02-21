package com.example.quizzer;

import com.example.quizzer.dataproviders.IQuestionsJSONProvider;
import com.example.quizzer.dataproviders.OpentdbHandler;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class OpentdbHandlerTest {
    private IQuestionsJSONProvider opentdbHandler;

    @BeforeEach
    void init(){
        opentdbHandler = new OpentdbHandler();
    }

    @Test
    void getQuestionsDoesNotThrow() {
        assertDoesNotThrow(() -> {
            opentdbHandler.getJSON(5);
        });
    }

    @Test
    void getQuestionReturnsFive(){
        try {
            JsonNode questionsJSON = opentdbHandler.getJSON(5);

            assertTrue(questionsJSON.isArray());
            assertEquals(questionsJSON.size(), 5);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }


    @Test
    void getQuestionReturnsTwenty(){
        try {
            JsonNode questionsJSON = opentdbHandler.getJSON(20);

            assertTrue(questionsJSON.isArray());
            assertEquals(questionsJSON.size(), 20);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void getQuestionNotNull(){
        try {
            JsonNode questions = opentdbHandler.getJSON(5);
            assertNotNull(questions);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void getQuestionsMultipleRequestsNotNull(){
        try {
            for(int i = 0; i < 10; i++) {
                assertNotNull(opentdbHandler.getJSON(5));
            }
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }
}