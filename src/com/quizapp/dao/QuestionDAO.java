// com.quizapp.dao.QuestionDAO.java
package com.quizapp.dao;

import com.quizapp.model.Question;
import com.quizapp.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class QuestionDAO {
    
    public Question getQuestionById(int questionId) {
        String sql = "SELECT q.*, c.category_name, d.level_name " +
                    "FROM questions q " +
                    "JOIN categories c ON q.category_id = c.category_id " +
                    "JOIN difficulty_levels d ON q.difficulty_id = d.difficulty_id " +
                    "WHERE q.question_id = ?";
                    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, questionId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToQuestion(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Question> getQuestionsByCategory(int categoryId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.*, c.category_name, d.level_name " +
                    "FROM questions q " +
                    "JOIN categories c ON q.category_id = c.category_id " +
                    "JOIN difficulty_levels d ON q.difficulty_id = d.difficulty_id " +
                    "WHERE q.category_id = ? AND q.is_active = TRUE";
                    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                questions.add(mapResultSetToQuestion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
    
    public List<Question> getRandomQuestions(int categoryId, int count) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.*, c.category_name, d.level_name " +
                    "FROM questions q " +
                    "JOIN categories c ON q.category_id = c.category_id " +
                    "JOIN difficulty_levels d ON q.difficulty_id = d.difficulty_id " +
                    "WHERE q.category_id = ? AND q.is_active = TRUE " +
                    "ORDER BY RAND() LIMIT ?";
                    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            pstmt.setInt(2, count);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                questions.add(mapResultSetToQuestion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
    
    public boolean addQuestion(Question question) {
        String sql = "INSERT INTO questions (category_id, difficulty_id, question_text, " +
                    "explanation, points, created_by) VALUES (?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, question.getCategoryId());
            pstmt.setInt(2, question.getDifficultyId());
            pstmt.setString(3, question.getQuestionText());
            pstmt.setString(4, question.getExplanation());
            pstmt.setInt(5, question.getPoints());
            pstmt.setInt(6, question.getCreatedBy());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int questionId = generatedKeys.getInt(1);
                    return addOptions(questionId, question.getOptions());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean addOptions(int questionId, Map<String, Boolean> options) {
        String sql = "INSERT INTO options (question_id, option_text, is_correct) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Map.Entry<String, Boolean> option : options.entrySet()) {
                pstmt.setInt(1, questionId);
                pstmt.setString(2, option.getKey());
                pstmt.setBoolean(3, option.getValue());
                pstmt.addBatch();
            }
            
            int[] results = pstmt.executeBatch();
            return results.length == options.size();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Question mapResultSetToQuestion(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setQuestionId(rs.getInt("question_id"));
        question.setCategoryId(rs.getInt("category_id"));
        question.setDifficultyId(rs.getInt("difficulty_id"));
        question.setQuestionText(rs.getString("question_text"));
        question.setExplanation(rs.getString("explanation"));
        question.setPoints(rs.getInt("points"));
        question.setCategoryName(rs.getString("category_name"));
        question.setDifficultyLevel(rs.getString("level_name"));
        
        // Load options
        question.setOptions(getOptionsForQuestion(question.getQuestionId()));
        
        return question;
    }
    
    private Map<String, Boolean> getOptionsForQuestion(int questionId) {
        Map<String, Boolean> options = new HashMap<>();
        String sql = "SELECT option_text, is_correct FROM options WHERE question_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, questionId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                options.put(rs.getString("option_text"), rs.getBoolean("is_correct"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return options;
    }
}