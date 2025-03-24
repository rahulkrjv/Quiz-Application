// com.quizapp.util.QuizManager.java
package com.quizapp.util;

import com.quizapp.dao.QuestionDAO;
import com.quizapp.model.Question;
import java.util.*;

public class QuizManager {
    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private Map<Integer, Integer> userAnswers; // Question index -> Selected answer

    public QuizManager() {
        loadQuestions();
        currentQuestionIndex = 0;
        score = 0;
        userAnswers = new HashMap<>();
    }

    private void loadQuestions() {
        // TODO: Later, load from database or file
        questions = new ArrayList<>();
        questions.add(new Question(
            "What is Java?",
            new String[]{
                "A type of coffee",
                "A programming language",
                "An island in Indonesia",
                "All of the above"
            },
            3,
            "Java is indeed all of these things - a type of coffee, a programming language, and an island in Indonesia!",
            10
            questions = new QuestionDAO().getRandomQuestions(
            currentCategory.getCategoryId(), 
            10  // Number of questions per quiz
    );
        ));
        
        // Add more questions
        questions.add(new Question(
            "Which keyword is used to define a constant in Java?",
            new String[]{
                "const",
                "final",
                "static",
                "constant"
            },
            1,
            "The 'final' keyword is used to define constants in Java.",
            10
        ));

        // Shuffle questions
        Collections.shuffle(questions);
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean submitAnswer(int selectedOption) {
        Question current = getCurrentQuestion();
        if (current != null) {
            userAnswers.put(currentQuestionIndex, selectedOption);
            if (current.isCorrectAnswer(selectedOption)) {
                score += current.getPoints();
                return true;
            }
        }
        return false;
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex < questions.size() - 1;
    }

    public void moveToNextQuestion() {
        if (hasNextQuestion()) {
            currentQuestionIndex++;
        }
    }

    public int getScore() { return score; }
    public int getTotalQuestions() { return questions.size(); }
    public int getCurrentQuestionNumber() { return currentQuestionIndex + 1; }
    public Map<Integer, Integer> getUserAnswers() { return userAnswers; }
}