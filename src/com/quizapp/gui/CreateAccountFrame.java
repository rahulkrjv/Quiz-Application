// com.quizapp.gui.CreateAccountFrame.java
package com.quizapp.gui;

import javax.swing.*;

import com.quizapp.util.UserAuthentication;

import java.awt.*;

public class CreateAccountFrame extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton createButton;

    public CreateAccountFrame() {
        setTitle("Create Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Fields
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        createButton = new JButton("Create Account");

        // Add components
        addFormField(mainPanel, "Name:", nameField);
        addFormField(mainPanel, "Email:", emailField);
        addFormField(mainPanel, "Password:", passwordField);
        addFormField(mainPanel, "Confirm Password:", confirmPasswordField);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createButton);

        createButton.addActionListener(e -> handleCreateAccount());

        add(mainPanel);
        setVisible(true);
    }

    private void addFormField(JPanel panel, String label, JComponent field) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.add(new JLabel(label));
        fieldPanel.add(field);
        panel.add(fieldPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void handleCreateAccount() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match!",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (UserAuthentication.isEmailRegistered(email)) {
            JOptionPane.showMessageDialog(this,
                "This email is already registered",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (UserAuthentication.registerUser(name, email, password)) {
            JOptionPane.showMessageDialog(this,
                "Account created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            // Return to login screen
            new LoginFrame();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error creating account",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}