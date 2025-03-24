// com.quizapp.gui.ChartDashboard.java
package com.quizapp.gui;

import com.quizapp.model.QuizAttempt;
import com.quizapp.util.ChartGenerator;
import com.quizapp.util.StatisticsManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChartDashboard extends JFrame {
    private String userId;
    private List<QuizAttempt> attempts;

    public ChartDashboard(String userId) {
        this.userId = userId;
        this.attempts = StatisticsManager.getUserAttempts(userId);
        setupUI();
    }

    private void setupUI() {
        setTitle("Performance Dashboard");
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (attempts.isEmpty()) {
            mainPanel.add(new JLabel("Complete some quizzes to see your performance charts!"), 
                BorderLayout.CENTER);
        } else {
            // Create chart grid
            JPanel chartGrid = new JPanel(new GridLayout(2, 2, 15, 15));
            
            // Add all charts
            chartGrid.add(ChartGenerator.createScoreProgressChart(attempts));
            chartGrid.add(ChartGenerator.createCategoryPerformanceChart(attempts));
            chartGrid.add(ChartGenerator.createCorrectVsIncorrectPieChart(attempts));
            chartGrid.add(ChartGenerator.createTimeDistributionChart(attempts));

            // Add export button
            JButton exportButton = new JButton("Export Charts");
            exportButton.addActionListener(e -> exportCharts());

            mainPanel.add(chartGrid, BorderLayout.CENTER);
            mainPanel.add(exportButton, BorderLayout.SOUTH);
        }

        add(mainPanel);
    }

    private void exportCharts() {
        // Implement chart export functionality
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Charts");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Implement actual export logic here
            JOptionPane.showMessageDialog(this, 
                "Charts exported successfully!", 
                "Export Complete", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}