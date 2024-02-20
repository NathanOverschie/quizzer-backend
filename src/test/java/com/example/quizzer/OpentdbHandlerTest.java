package com.example.quizzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class OpentdbHandlerTest {
    final private OpentdbHandler opentdbHandler = new OpentdbHandler();

    @Test
    void getQuestionsDoesNotThrow() {
        assertDoesNotThrow(opentdbHandler::getQuestions);
    }
}