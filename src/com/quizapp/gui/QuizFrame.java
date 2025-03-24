// com.quizapp.gui.QuizFrame.java
package com.quizapp.gui;

import com.quizapp.model.Question;
import com.quizapp.model.QuizAttempt;
import com.quizapp.model.User;
import com.quizapp.util.QuizManager;
import com.quizapp.util.SessionManager;
import com.quizapp.gui.StatisticsFrame;
import com.quizapp.util.StatisticsManager;
import com.quizapp.util.UserAuthentication;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuizFrame extends JFrame {
    private QuizManager quizManager;
    private JLabel questionLabel;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private ButtonGroup optionsGroup;
    private JRadioButton[] optionButtons;
    private JButton submitButton;
    private JButton nextButton;
    private JTextArea explanationArea;
    private Timer questionTimer;
    private int timeRemaining;
    private QuizAttempt currentAttempt;
    private long questionStartTime;

    public QuizFrame() {
        quizManager = new QuizManager();
        currentAttempt = new QuizAttempt(
            SessionManager.getCurrentUser().getEmail(),
            "General Knowledge" // or whatever category
        );
        setupUI();
        displayQuestion();
        startTimer();
    }

    private void setupUI() {
        setTitle("Quiz Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel for score and timer
        JPanel topPanel = new JPanel(new BorderLayout());
        scoreLabel = new JLabel("Score: 0");
        timerLabel = new JLabel("Time: 30");
        topPanel.add(scoreLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);

        // Question panel
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionPanel.add(questionLabel);

        // Options panel
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        optionsGroup = new ButtonGroup();
        optionButtons = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        submitButton = new JButton("Submit");
        nextButton = new JButton("Next");
        nextButton.setEnabled(false);
        buttonPanel.add(submitButton);
        buttonPanel.add(nextButton);

        // Explanation area
        explanationArea = new JTextArea(3, 40);
        explanationArea.setEditable(false);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setLineWrap(true);
        explanationArea.setVisible(false);
        JScrollPane explanationScroll = new JScrollPane(explanationArea);

        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(explanationScroll, BorderLayout.SOUTH);

        // Add action listeners
        submitButton.addActionListener(e -> handleSubmission());
        nextButton.addActionListener(e -> moveToNextQuestion());

        add(mainPanel);
    }

    private void displayQuestion() {
        Question currentQuestion = quizManager.getCurrentQuestion();
        if (currentQuestion != null) {
            questionLabel.setText(currentQuestion.getQuestionText());
            List<String> options = currentQuestion.getOptions();
            for (int i = 0; i < options.size(); i++) {
                optionButtons[i].setText(options.get(i));
                optionButtons[i].setSelected(false);
            }
            explanationArea.setVisible(false);
            submitButton.setEnabled(true);
            nextButton.setEnabled(false);
            timeRemaining = 30;
            updateTimerLabel();
        } else {
            showQuizComplete();
        }
    }

    private void handleSubmission() {
        int selectedOption = -1;
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selectedOption = i;
                break;
            }
        }

        if (selectedOption == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an answer",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean correct = quizManager.submitAnswer(selectedOption);
        questionTimer.stop();
        
        // Show explanation
        Question currentQuestion = quizManager.getCurrentQuestion();
        explanationArea.setText("Explanation: " + currentQuestion.getExplanation());
        explanationArea.setVisible(true);
        
        // Update UI
        scoreLabel.setText("Score: " + quizManager.getScore());
        submitButton.setEnabled(false);
        nextButton.setEnabled(true);

        // Show feedback
        String answer = correct ? "Correct!" : "Incorrect!";
        JOptionPane.showMessageDialog(this, answer);

        // Save question result
        currentAttempt.getQuestionResults().put(
            quizManager.getCurrentQuestionNumber(),
            correct
        );
        
        // Save time taken
        long timeTaken = 30 - timeRemaining;
        currentAttempt.getTimePerQuestion().put(
            quizManager.getCurrentQuestionNumber(),
            (int)timeTaken
        );

        // Prompt for feedback
        String feedback = JOptionPane.showInputDialog(this,
            "Would you like to provide feedback for this question?",
            "Question Feedback",
            JOptionPane.QUESTION_MESSAGE);
        
        if (feedback != null && !feedback.trim().isEmpty()) {
            currentAttempt.getUserFeedback().put(
                quizManager.getCurrentQuestionNumber(),
                feedback
            );
        }
    }

    private void moveToNextQuestion() {
        if (quizManager.hasNextQuestion()) {
            quizManager.moveToNextQuestion();
            displayQuestion();
            startTimer();
        } else {
            showQuizComplete();
        }
    }

    private void startTimer() {
        if (questionTimer != null) {
            questionTimer.stop();
        }
        
        timeRemaining = 30;
        questionTimer = new Timer(1000, e -> {
            timeRemaining--;
            updateTimerLabel();
            if (timeRemaining <= 0) {
                ((Timer)e.getSource()).stop();
                handleTimeUp();
            }
        });
        questionTimer.start();
    }

    private void updateTimerLabel() {
        timerLabel.setText("Time: " + timeRemaining);
    }

    private void handleTimeUp() {
        JOptionPane.showMessageDialog(this,
            "Time's up!",
            "Time Expired",
            JOptionPane.WARNING_MESSAGE);
        submitButton.setEnabled(false);
        nextButton.setEnabled(true);
    }

    private void showQuizComplete() {
        questionTimer.stop();
        int finalScore = quizManager.getScore();
        int totalQuestions = quizManager.getTotalQuestions();
        
        String message = String.format(
            "Quiz Complete!\nFinal Score: %d/%d\nPercentage: %.1f%%",
            finalScore,
            totalQuestions * 10,
            (finalScore * 100.0) / (totalQuestions * 10)
        );

        JOptionPane.showMessageDialog(this,
            message,
            "Quiz Complete",
            JOptionPane.INFORMATION_MESSAGE);

        // Update user's high score if needed
        // TODO: Implement high score update

        // Return to main menu or exit
        int choice = JOptionPane.showConfirmDialog(this,
            "Would you like to try another quiz?",
            "Play Again",
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new QuizFrame().setVisible(true);
        } else {
            dispose();
            // TODO: Return to main menu
        }

        // Save final statistics
        currentAttempt.setScore(finalScore);
        currentAttempt.setTotalQuestions(totalQuestions);
        currentAttempt.setTotalTimeSpent(
            currentAttempt.getTimePerQuestion().values().stream()
                .mapToInt(Integer::intValue)
                .sum()
        );
        
        StatisticsManager.saveQuizAttempt(currentAttempt);

        // Show statistics
        int choices = JOptionPane.showConfirmDialog(this,
            "Would you like to view your statistics?",
            "View Statistics",
            JOptionPane.YES_NO_OPTION);
            
        if (choices == JOptionPane.YES_OPTION) {
            new StatisticsFrame().setVisible(true);
        }
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu statsMenu = new JMenu("Statistics");
        JMenuItem viewStats = new JMenuItem("View Statistics");
        viewStats.addActionListener(e -> new StatisticsFrame().setVisible(true));
        statsMenu.add(viewStats);
        
        menuBar.add(statsMenu);
        setJMenuBar(menuBar);
    }

    private void updateHighScore(int score) {
        User currentUser = SessionManager.getCurrentUser();
        if (score > currentUser.getHighScore()) {
            currentUser.setHighScore(score);
            UserAuthentication.updateUser(currentUser);
        }
    }

    private void returnToMainMenu() {
        new MainMenuFrame().setVisible(true);
        this.dispose();
    }
}