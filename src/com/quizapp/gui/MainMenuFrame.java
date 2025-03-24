package com.quizapp.gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        setTitle("Quiz Application - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton startQuizButton = new JButton("Start Quiz");
        JButton statisticsButton = new JButton("View Statistics");
        JButton leaderboardButton = new JButton("Leaderboard");
        JButton logoutButton = new JButton("Logout");

        startQuizButton.addActionListener(e -> startQuiz());
        statisticsButton.addActionListener(e -> viewStatistics());
        leaderboardButton.addActionListener(e -> viewLeaderboard());
        logoutButton.addActionListener(e -> logout());

        mainPanel.add(startQuizButton);
        mainPanel.add(statisticsButton);
        mainPanel.add(leaderboardButton);
        mainPanel.add(logoutButton);

        add(mainPanel);
    }

    private void startQuiz() {
        new QuizFrame().setVisible(true);
        this.dispose();
    }

    private void viewStatistics() {
        new StatisticsFrame().setVisible(true);
    }

    private void viewLeaderboard() {
        new ChartDashboard(SessionManager.getCurrentUser().getEmail()).setVisible(true);
    }

    private void logout() {
        SessionManager.clearSession();
        new LoginFrame().setVisible(true);
        this.dispose();
    }
}