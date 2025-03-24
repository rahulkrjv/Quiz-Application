// com.quizapp.gui.LoginFrame.java
package com.quizapp.gui;

import javax.swing.*;

import com.quizapp.model.User;
import com.quizapp.util.GoogleAuthHelper;
import com.quizapp.util.SessionManager;
import com.quizapp.util.UserAuthentication;

import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    private JButton facebookButton;
    private JButton googleButton;

    public LoginFrame() {
        setTitle("Quiz Application - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Email field
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailField = new JTextField(20);
        emailPanel.add(new JLabel("Email:"));
        emailPanel.add(emailField);
        
        // Password field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordField = new JPasswordField(20);
        passwordPanel.add(new JLabel("Password:"));
        passwordPanel.add(passwordField);

        // Buttons
        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create Account");
        facebookButton = new JButton("Connect with Facebook");
        googleButton = new JButton("Connect with Google");

        // Add components to main panel
        mainPanel.add(emailPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createAccountButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(facebookButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(googleButton);

        // Add action listeners
        loginButton.addActionListener(e -> handleLogin());
        createAccountButton.addActionListener(e -> openCreateAccount());
        facebookButton.addActionListener(e -> handleFacebookLogin());
        googleButton.addActionListener(e -> handleGoogleLogin());

        add(mainPanel);
        setVisible(true);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
    
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both email and password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        User user = UserAuthentication.loginUser(email, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, 
                "Welcome back, " + user.getName() + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);
            openQuizInterface(user);
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid email or password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openCreateAccount() {
        // TODO: Open create account frame
        new CreateAccountFrame();
        this.dispose();
    }

    private void handleFacebookLogin() {
        // TODO: Implement Facebook login
        JOptionPane.showMessageDialog(this, "Facebook login not implemented yet");
    }

    private void handleGoogleLogin() {
        GoogleAuthHelper googleAuth = new GoogleAuthHelper();
        String authUrl = googleAuth.getAuthorizationUrl();
        
        // Open the default browser with the authorization URL
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(authUrl));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error opening browser for Google login",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openQuizInterface(User user) {
        SessionManager.setCurrentUser(user);
        new QuizFrame().setVisible(true);
        this.dispose();
    }
}
