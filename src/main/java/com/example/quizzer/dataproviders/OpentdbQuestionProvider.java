package com.example.quizzer.dataproviders;

import com.example.quizzer.QuestionWithAnswerAndFalseAnswers;
import com.example.quizzer.utils.OpentdbException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;


/**
 * Class to provide questions by deserializing JSON structured in the following way:
 * [
 *  {
 *   question: String,
 *   correct_answer: String,
 *   incorrect_answers: [String],
 *   ...,
 *  }
 * ]
 *
 */
public class OpentdbQuestionProvider implements IQuestionProvider {
    private final IQuestionsJSONProvider jsonProvider;

    public OpentdbQuestionProvider(IQuestionsJSONProvider jsonProvider){
        this.jsonProvider = jsonProvider;
    }

    /**
     * When the json has the input described above, return POJO object, otherwise throws exception
     * @param jsonNode input json
     */
    private QuestionWithAnswerAndFalseAnswers POJOFromJSONQuestion(JsonNode jsonNode) throws OpentdbException {
        JsonNode questionNode = jsonNode.get("question");
        JsonNode answerNode = jsonNode.get("correct_answer");
        JsonNode falseAnswersNode = jsonNode.get("incorrect_answers");

        if(questionNode == null || answerNode == null || falseAnswersNode == null){
            throw new OpentdbException();
        }

        String answer = answerNode.asText();
        String text = questionNode.asText();
        List<String> falseAnswers =
                StreamSupport.stream(falseAnswersNode.spliterator(), false)
                .map(JsonNode::asText)
                .toList();

        return new QuestionWithAnswerAndFalseAnswers(
                text,
                answer,
                falseAnswers);
    }

    @Override
    public List<QuestionWithAnswerAndFalseAnswers> getMaxQuestionsWithAnswerAndFalseAnswers() throws Exception {
        JsonNode questionsJSON = jsonProvider.getMaxJSON();

        List<QuestionWithAnswerAndFalseAnswers> result = new ArrayList<>();

        if(questionsJSON.isArray()){
            for (JsonNode questionJSON : questionsJSON) {
                result.add(POJOFromJSONQuestion(questionJSON));
            }
        }else{
            result.add(POJOFromJSONQuestion(questionsJSON));
        }

        return result;
    }
}