// com.quizapp.util.ChartGenerator.java
package com.quizapp.util;

import com.quizapp.model.QuizAttempt;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartGenerator {

    public static ChartPanel createScoreProgressChart(List<QuizAttempt> attempts) {
        TimeSeries series = new TimeSeries("Score Progress");
        
        attempts.forEach(attempt -> {
            series.add(new Day(attempt.getTimestamp().toLocalDate().toEpochDay()), 
                      attempt.getPercentageScore());
        });

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Score Progress Over Time",
            "Date",
            "Score (%)",
            dataset,
            true,
            true,
            false
        );

        customizeChart(chart);
        return new ChartPanel(chart);
    }

    public static ChartPanel createCategoryPerformanceChart(List<QuizAttempt> attempts) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        Map<String, Double> categoryAverages = attempts.stream()
            .collect(Collectors.groupingBy(
                QuizAttempt::getCategory,
                Collectors.averagingDouble(QuizAttempt::getPercentageScore)
            ));

        categoryAverages.forEach((category, average) -> 
            dataset.addValue(average, "Average Score", category));

        JFreeChart chart = ChartFactory.createBarChart(
            "Performance by Category",
            "Category",
            "Average Score (%)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        customizeChart(chart);
        return new ChartPanel(chart);
    }

    public static ChartPanel createCorrectVsIncorrectPieChart(List<QuizAttempt> attempts) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        long totalCorrect = attempts.stream()
            .flatMap(attempt -> attempt.getQuestionResults().values().stream())
            .filter(correct -> correct)
            .count();
            
        long totalQuestions = attempts.stream()
            .mapToInt(QuizAttempt::getTotalQuestions)
            .sum();
            
        dataset.setValue("Correct", totalCorrect);
        dataset.setValue("Incorrect", totalQuestions - totalCorrect);

        JFreeChart chart = ChartFactory.createPieChart(
            "Overall Performance",
            dataset,
            true,
            true,
            false
        );

        customizePieChart(chart);
        return new ChartPanel(chart);
    }

    public static ChartPanel createTimeDistributionChart(List<QuizAttempt> attempts) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        Map<Integer, Double> timeDistribution = attempts.stream()
            .flatMap(attempt -> attempt.getTimePerQuestion().entrySet().stream())
            .collect(Collectors.groupingBy(
                Map.Entry::getValue,
                Collectors.averagingDouble(e -> 1.0)
            ));

        timeDistribution.forEach((time, count) -> 
            dataset.addValue(count, "Frequency", time.toString()));

        JFreeChart chart = ChartFactory.createBarChart(
            "Time Distribution per Question",
            "Time (seconds)",
            "Frequency",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );

        customizeChart(chart);
        return new ChartPanel(chart);
    }

    private static void customizeChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        
        if (chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setOutlinePaint(Color.BLACK);
        } else if (chart.getPlot() instanceof XYPlot) {
            XYPlot plot = chart.getXYPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setOutlinePaint(Color.BLACK);
        }
    }

    private static void customizePieChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Correct", new Color(46, 204, 113));
        plot.setSectionPaint("Incorrect", new Color(231, 76, 60));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(Color.BLACK);
    }
}