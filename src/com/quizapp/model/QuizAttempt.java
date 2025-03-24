// com.quizapp.model.QuizAttempt.java
package com.quizapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class QuizAttempt implements Serializable {
    private String userId;
    private LocalDateTime timestamp;
    private int score;
    private int totalQuestions;
    private Map<Integer, Boolean> questionResults; // Question ID -> Correct/Incorrect
    private Map<Integer, Integer> timePerQuestion; // Question ID -> Time taken
    private Map<Integer, String> userFeedback; // Question ID -> Feedback
    private String category;
    private long totalTimeSpent;

    public QuizAttempt(String userId, String category) {
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
        this.category = category;
        this.questionResults = new HashMap<>();
        this.timePerQuestion = new HashMap<>();
        this.userFeedback = new HashMap<>();
    }

    // Getters and setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    public Map<Integer, Boolean> getQuestionResults() { return questionResults; }
    public Map<Integer, Integer> getTimePerQuestion() { return timePerQuestion; }
    public Map<Integer, String> getUserFeedback() { return userFeedback; }
    public String getCategory() { return category; }
    public long getTotalTimeSpent() { return totalTimeSpent; }
    public void setTotalTimeSpent(long totalTimeSpent) { this.totalTimeSpent = totalTimeSpent; }

    public double getPercentageScore() {
        return (score * 100.0) / (totalQuestions * 10);
    }
}