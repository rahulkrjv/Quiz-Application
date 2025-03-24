// com.quizapp.model.Question.java
package com.quizapp.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Question implements Serializable {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;
    private String explanation;
    private int points;

    public Question(String questionText, String[] options, int correctOptionIndex, String explanation, int points) {
        this.questionText = questionText;
        this.options = Arrays.asList(options);
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
        this.points = points;
    }

    // Getters
    public String getQuestionText() { return questionText; }
    public List<String> getOptions() { return options; }
    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public String getExplanation() { return explanation; }
    public int getPoints() { return points; }
    
    public boolean isCorrectAnswer(int selectedOption) {
        return selectedOption == correctOptionIndex;
    }
}

// public class Question {
//     private int questionId;
//     private int categoryId;
//     private int difficultyId;
//     private String questionText;
//     private String explanation;
//     private int points;
//     private boolean isActive;
//     private String categoryName;
//     private String difficultyLevel;
//     private int createdBy;
//     private Map<String, Boolean> options;
    
//     public Question() {
//         this.options = new HashMap<>();
//     }
    
//     // Getters and setters
//     // ... (implement all getters and setters)
    
//     public void addOption(String optionText, boolean isCorrect) {
//         this.options.put(optionText, isCorrect);
//     }
    
//     public boolean isCorrectOption(String optionText) {
//         return options.getOrDefault(optionText, false);
//     }
// }