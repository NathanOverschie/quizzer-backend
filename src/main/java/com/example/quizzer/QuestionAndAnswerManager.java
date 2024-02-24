package com.example.quizzer;

import com.example.quizzer.dataproviders.IQuestionProvider;
import com.example.quizzer.utils.CorrectAnswerNotFoundException;
import com.example.quizzer.utils.LimitedHashMap;
import com.example.quizzer.utils.NotEnoughQuestionsException;

import java.util.*;

/**
 * Manages questions and answers for the Quizzer application.
 * Implements the IQuestionAndAnswerManager interface.
 */
public class QuestionAndAnswerManager implements IQuestionAndAnswerManager {

    /** Maximum number of saved answers. */
    private int maxSavedAnswers;

    /** Instance of the IQuestionProvider interface */
    private final IQuestionProvider questionProvider;

    /** Counter for generating unique ID's*/
    private int counter = 0;
    private List<QuestionWithAnswerAndFalseAnswers> questionStack = new LinkedList<>();

    /** Map to store correct answers with their corresponding IDs. */
    private final Map<Integer, String> correctAnswers = new LimitedHashMap<>(maxSavedAnswers);


    QuestionAndAnswerManager(IQuestionProvider questionProvider, int maxSavedAnswers){
        this.questionProvider = questionProvider;
        this.maxSavedAnswers = maxSavedAnswers;
    }

    /**
     * Generates a new unique ID.
     *
     * @return Unique ID.
     */
    private int newId(){
        return counter++;
    }

    /**
     * Transforms a question with correct answer and false answers into a question with possible answers
     * @param questionWithAnswerAndFalseAnswers questions with correct and false answers
     * @return question with hidden correct answer only possible answers
     */
    private QuestionWithPossibleAnswers HideAnswer(QuestionWithAnswerAndFalseAnswers questionWithAnswerAndFalseAnswers) {
        List<String> possibleAnswers = new ArrayList<>(questionWithAnswerAndFalseAnswers.falseAnswers());
        possibleAnswers.add(questionWithAnswerAndFalseAnswers.answer());
        Collections.shuffle(possibleAnswers);

        return new QuestionWithPossibleAnswers(
                        newId(),
                        questionWithAnswerAndFalseAnswers.question(),
                        possibleAnswers);
    }

    /**
     * Gives questions with possible answers
     * @param amount number of questions to return
     * @return questions with possible answers
     * @throws NotEnoughQuestionsException there are not enough questions to return
     */
    @Override
    public List<QuestionWithPossibleAnswers> getQuestionsWithPossibleAnswers(int amount) throws NotEnoughQuestionsException {
        List<QuestionWithAnswerAndFalseAnswers> questionsWithAnswerAndFalseAnswers = getViaStack(amount);

        List<QuestionWithPossibleAnswers> result = new ArrayList<>();

        for (QuestionWithAnswerAndFalseAnswers questionsWithAnswerAndFalseAnswer : questionsWithAnswerAndFalseAnswers) {
            QuestionWithPossibleAnswers questionWithPossibleAnswers = HideAnswer(questionsWithAnswerAndFalseAnswer);
            result.add(questionWithPossibleAnswers);
            correctAnswers.put(questionWithPossibleAnswers.ID(), questionsWithAnswerAndFalseAnswer.answer());
        }

        return result;
    }

    /**
     * Retrieves questions with answers and false answers from the question provider.
     * Synchronizes access to ensure thread safety.
     *
     * @param amount Number of questions to retrieve.
     * @return List of questions with answers and false answers.
     * @throws NotEnoughQuestionsException If there are not enough questions available.
     */
    private synchronized List<QuestionWithAnswerAndFalseAnswers> getViaStack(int amount) throws NotEnoughQuestionsException {
        while(questionStack.size() < amount){
            try {
                questionStack.addAll(questionProvider.getMaxQuestionsWithAnswerAndFalseAnswers());
            } catch (Exception e) {
                throw new NotEnoughQuestionsException();
            }
        }

        List<QuestionWithAnswerAndFalseAnswers> questionsWithAnswerAndFalseAnswers = questionStack.subList(0, amount);
        questionStack = questionStack.subList(amount, questionStack.size());
        return questionsWithAnswerAndFalseAnswers;
    }

    /**
     * Checks if the provided answer is correct for the given question ID.
     *
     * @param ID ID of the question.
     * @param Answer Answer provided by the user.
     * @return True if the answer is correct, false otherwise.
     * @throws CorrectAnswerNotFoundException If the correct answer for the given ID is not found.
     */
    @Override
    public boolean checkAnswer(int ID, String Answer) throws CorrectAnswerNotFoundException {
        String correctAnswer = correctAnswers.get(ID);

        if(correctAnswer == null){
            throw new CorrectAnswerNotFoundException();
        }

        return Answer.equals(correctAnswer);
    }
}
