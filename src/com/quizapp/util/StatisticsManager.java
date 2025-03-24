// com.quizapp.util.StatisticsManager.java
package com.quizapp.util;

import com.quizapp.model.QuizAttempt;
import com.quizapp.model.User;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsManager {
    private static final String STATS_FILE = "quiz_statistics.dat";
    private static Map<String, List<QuizAttempt>> userAttempts = new HashMap<>();

    static {
        loadStatistics();
    }

    public static void saveQuizAttempt(QuizAttempt attempt) {
        String userId = attempt.getUserId();
        userAttempts.computeIfAbsent(userId, k -> new ArrayList<>()).add(attempt);
        saveStatistics();
    }

    public static List<QuizAttempt> getUserAttempts(String userId) {
        return userAttempts.getOrDefault(userId, new ArrayList<>());
    }

    public static Map<String, Double> getLeaderboard() {
        Map<String, Double> averageScores = new HashMap<>();
        
        userAttempts.forEach((userId, attempts) -> {
            double avgScore = attempts.stream()
                .mapToDouble(QuizAttempt::getPercentageScore)
                .average()
                .orElse(0.0);
            averageScores.put(userId, avgScore);
        });

        return averageScores.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }

    private static void loadStatistics() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STATS_FILE))) {
            userAttempts = (Map<String, List<QuizAttempt>>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No statistics file found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveStatistics() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STATS_FILE))) {
            oos.writeObject(userAttempts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> getUserStatistics(String userId) {
        List<QuizAttempt> attempts = getUserAttempts(userId);
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalQuizzes", attempts.size());
        stats.put("averageScore", attempts.stream()
            .mapToDouble(QuizAttempt::getPercentageScore)
            .average()
            .orElse(0.0));
        stats.put("bestScore", attempts.stream()
            .mapToDouble(QuizAttempt::getPercentageScore)
            .max()
            .orElse(0.0));
        stats.put("totalTimePlayed", attempts.stream()
            .mapToLong(QuizAttempt::getTotalTimeSpent)
            .sum());
        
        return stats;
    }
}   