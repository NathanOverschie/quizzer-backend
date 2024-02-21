package com.example.quizzer;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OpentdbQuestionProviderTest {
    private QuestionProvider questionProvider;
    private fakeJSONProvider jsonProvider;

    static class fakeJSONProvider implements JSONProvider {
        private String jsonString = null;
        private Exception exception = null;
        public void setJsonString(String jsonString) {
            this.jsonString = jsonString;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        @Override
        public JsonNode getJSON() throws Exception {
            if(exception != null){
                throw exception;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(jsonString);
        }
    }

    @BeforeEach
    public void init(){
        jsonProvider = new fakeJSONProvider();
        questionProvider = new OpentdbQuestionProvider(jsonProvider);
    }

    @Test
    void getQuestionWithAnswerAndFalseAnswersHappyFlow(){
        //Setup
        List<QuestionWithAnswerAndFalseAnswers> expected = new ArrayList<>();

        expected.add(new QuestionWithAnswerAndFalseAnswers(
                "What is the name of the game developer who created &quot;Call Of Duty: Zombies&quot;?",
                "Treyarch",
                new ArrayList<>(Arrays.asList("Sledgehammer Games", "Infinity Ward", "Naughty Dog"))));

        expected.add(new QuestionWithAnswerAndFalseAnswers(
                "In the Harry Potter universe, who does Draco Malfoy end up marrying?",
                "Astoria Greengrass",
                new ArrayList<>(Arrays.asList("Pansy Parkinson", "Millicent Bulstrode", "Hermione Granger"))
        ));

        jsonProvider.setJsonString(
                "[" + expected.stream()
                        .map(OpentdbQuestionProviderTest::getJsonStringOfQuestionWithAnswerAndFalseAnswers)
                        .collect(Collectors.joining(",")) + "]");


        try {
            //Act
            List<QuestionWithAnswerAndFalseAnswers> actual = questionProvider.getQuestionWithAnswerAndFalseAnswers();

            //Assert
            assertEquals(expected, actual);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void getQuestionWithAnswerAndFalseAnswersExceptionThrown(){
        //Setup
        jsonProvider.setException(new Exception());

        try {
            //Act
            questionProvider.getQuestionWithAnswerAndFalseAnswers();

            //Assert
            fail();
        }catch (Exception ignored){
        }
    }

    @Test
    void getQuestionWithAnswerAndFalseAnswersMalformedJSON(){
        //Setup
        jsonProvider.setJsonString("[[");

        try {
            //Act
            questionProvider.getQuestionWithAnswerAndFalseAnswers();

            //Assert
            fail();
        } catch (Exception e) {
            assertInstanceOf(JsonEOFException.class, e);
        }
    }

    @Test
    void getQuestionWithAnswerAndFalseAnswersJSONWrongStructure(){
        //Setup
        jsonProvider.setJsonString("[{\"a\":\"b\"}]");

        try {
            //Act
            questionProvider.getQuestionWithAnswerAndFalseAnswers();

            //Assert
            fail();
        } catch (Exception e) {
            assertInstanceOf(OpentdbException.class, e);
        }
    }

    private static String getJsonStringOfQuestionWithAnswerAndFalseAnswers(QuestionWithAnswerAndFalseAnswers questionWithAnswerAndFalseAnswers) {
        return "{" +
                "\"type\":\"multiple\"," +
                "\"difficulty\":\"easy\"," +
                "\"category\":\"Entertainment: Video Games\"," +
                "\"question\":" + "\"" + questionWithAnswerAndFalseAnswers.question() + "\"," +
                "\"correct_answer\":\"" + questionWithAnswerAndFalseAnswers.answer() + "\"," +
                "\"incorrect_answers\":[" +
                questionWithAnswerAndFalseAnswers.falseAnswers()
                        .stream()
                        .map(x -> '"' + x + '"')
                        .collect(Collectors.joining(",")) +
                "]" +
                "}";
    }
}