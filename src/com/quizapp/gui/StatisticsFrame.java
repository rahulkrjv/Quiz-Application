// com.quizapp.gui.StatisticsFrame.java
package com.quizapp.gui;

import com.quizapp.model.QuizAttempt;
import com.quizapp.util.ChartGenerator;
import com.quizapp.util.SessionManager;
import com.quizapp.util.StatisticsManager;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class StatisticsFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private String userId;

    public StatisticsFrame() {
        this.userId = SessionManager.getCurrentUser().getEmail();
        setupUI();
        loadStatistics();
    }

    private void setupUI() {
        setTitle("Quiz Statistics");
        setSize(800, 600);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Overview", createOverviewPanel());
        tabbedPane.addTab("History", createHistoryPanel());
        tabbedPane.addTab("Leaderboard", createLeaderboardPanel());

        add(tabbedPane);
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Map<String, Object> stats = StatisticsManager.getUserStatistics(userId);
        
        JPanel statsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        statsPanel.add(new JLabel("Total Quizzes Taken:"));
        statsPanel.add(new JLabel(String.valueOf(stats.get("totalQuizzes"))));
        statsPanel.add(new JLabel("Average Score:"));
        statsPanel.add(new JLabel(String.format("%.2f%%", (double)stats.get("averageScore"))));
        statsPanel.add(new JLabel("Best Score:"));
        statsPanel.add(new JLabel(String.format("%.2f%%", (double)stats.get("bestScore"))));
        statsPanel.add(new JLabel("Total Time Played:"));
        statsPanel.add(new JLabel(formatTime((long)stats.get("totalTimePlayed"))));

        panel.add(statsPanel, BorderLayout.NORTH);
        
        // Charts
        JPanel chartsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        List<QuizAttempt> attempts = StatisticsManager.getUserAttempts(userId);

        // Add charts only if there are attempts
        if (!attempts.isEmpty()) {
            chartsPanel.add(ChartGenerator.createScoreProgressChart(attempts));
            chartsPanel.add(ChartGenerator.createCategoryPerformanceChart(attempts));
            chartsPanel.add(ChartGenerator.createCorrectVsIncorrectPieChart(attempts));
            chartsPanel.add(ChartGenerator.createTimeDistributionChart(attempts));
        } else {
            chartsPanel.add(new JLabel("Complete some quizzes to see statistics!"));
        }

        panel.add(chartsPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        List<QuizAttempt> attempts = StatisticsManager.getUserAttempts(userId);
        String[] columnNames = {"Date", "Category", "Score", "Time Spent", "Feedback"};
        Object[][] data = new Object[attempts.size()][5];
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (int i = 0; i < attempts.size(); i++) {
            QuizAttempt attempt = attempts.get(i);
            data[i][0] = attempt.getTimestamp().format(formatter);
            data[i][1] = attempt.getCategory();
            data[i][2] = String.format("%.1f%%", attempt.getPercentageScore());
            data[i][3] = formatTime(attempt.getTotalTimeSpent());
            data[i][4] = "View Details";
        }
        
        JTable table = new JTable(data, columnNames);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    showAttemptDetails(attempts.get(row));
                }
            }
        });
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLeaderboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        Map<String, Double> leaderboard = StatisticsManager.getLeaderboard();
        String[] columnNames = {"Rank", "User", "Average Score"};
        Object[][] data = new Object[leaderboard.size()][3];
        
        int rank = 1;
        for (Map.Entry<String, Double> entry : leaderboard.entrySet()) {
            data[rank-1][0] = rank;
            data[rank-1][1] = entry.getKey();
            data[rank-1][2] = String.format("%.1f%%", entry.getValue());
            rank++;
        }
        
        JTable table = new JTable(data, columnNames);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void showAttemptDetails(QuizAttempt attempt) {
        JDialog dialog = new JDialog(this, "Attempt Details", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Question results
        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        
        attempt.getQuestionResults().forEach((questionId, correct) -> {
            JPanel questionPanel = new JPanel(new BorderLayout());
            questionPanel.add(new JLabel("Question " + questionId + ": " + 
                (correct ? "Correct" : "Incorrect")), BorderLayout.WEST);
            questionPanel.add(new JLabel("Time: " + 
                attempt.getTimePerQuestion().get(questionId) + "s"), BorderLayout.EAST);
            questionsPanel.add(questionPanel);
        });

        panel.add(new JScrollPane(questionsPanel), BorderLayout.CENTER);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%dm %ds", minutes, remainingSeconds);
    }

    private JPanel createAnalysisPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        List<QuizAttempt> attempts = StatisticsManager.getUserAttempts(userId);
    
        if (attempts.isEmpty()) {
            panel.add(new JLabel("No data available for analysis"), BorderLayout.CENTER);
            return panel;
        }
    
        JTabbedPane chartTabs = new JTabbedPane();
        
        // Score Progress Chart
        chartTabs.addTab("Score Progress", 
            ChartGenerator.createScoreProgressChart(attempts));
        
        // Category Performance
        chartTabs.addTab("Category Performance", 
            ChartGenerator.createCategoryPerformanceChart(attempts));
        
        // Overall Performance
        chartTabs.addTab("Overall Performance", 
            ChartGenerator.createCorrectVsIncorrectPieChart(attempts));
        
        // Time Distribution
        chartTabs.addTab("Time Analysis", 
            ChartGenerator.createTimeDistributionChart(attempts));
    
        panel.add(chartTabs, BorderLayout.CENTER);
        return panel;
    }
}