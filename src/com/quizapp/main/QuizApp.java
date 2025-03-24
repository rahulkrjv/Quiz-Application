// com.quizapp.main.QuizApp.java
package com.quizapp.main;

import javax.swing.*;
import java.awt.*;

public class QuizApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new LoginFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}