package com.example.quizzer;

import com.example.quizzer.dataproviders.IQuestionProvider;
import com.example.quizzer.dataproviders.IQuestionsJSONProvider;
import com.example.quizzer.dataproviders.OpentdbQuestionProvider;
import com.example.quizzer.utils.OpentdbException;
import com.fasterxml.jackson.core.io.JsonEOFException;
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
    private IQuestionProvider questionProvider;
    private fakeJSONProvider jsonProvider;

    static class fakeJSONProvider implements IQuestionsJSONProvider {
        private String jsonString = null;
        private Exception exception = null;
        public void setJsonString(String jsonString) {
            this.jsonString = jsonString;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        @Override
        public JsonNode getJSON(int amount) throws Exception {
            if(exception != null){
                throw exception;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(jsonString);
        }

        @Override
        public JsonNode getMaxJSON() throws Exception {
            return getJSON(50);
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
            List<QuestionWithAnswerAndFalseAnswers> actual = questionProvider.getMaxQuestionsWithAnswerAndFalseAnswers();

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

        //Act
        assertThrows(Exception.class, () ->
                questionProvider.getMaxQuestionsWithAnswerAndFalseAnswers());
    }

    @Test
    void getQuestionWithAnswerAndFalseAnswersMalformedJSON(){
        //Setup
        jsonProvider.setJsonString("[[");

        //Act & Assert
        assertThrows(JsonEOFException.class, () ->
                questionProvider.getMaxQuestionsWithAnswerAndFalseAnswers());
    }

    @Test
    void getQuestionWithAnswerAndFalseAnswersJSONWrongStructure(){
        //Setup
        jsonProvider.setJsonString("[{\"a\":\"b\"}]");

        //Act & Assert
        assertThrows(OpentdbException.class, () ->
                questionProvider.getMaxQuestionsWithAnswerAndFalseAnswers());
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