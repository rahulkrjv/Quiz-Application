// com.quizapp.gui.PasswordResetDialog.java
package com.quizapp.gui;

import javax.swing.*;
import java.awt.*;

public class PasswordResetDialog extends JDialog {
    private JTextField emailField;
    
    public PasswordResetDialog(JFrame parent) {
        super(parent, "Reset Password", true);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        emailField = new JTextField(20);
        JButton resetButton = new JButton("Send Reset Link");
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Enter your email:"), gbc);
        
        gbc.gridy = 1;
        panel.add(emailField, gbc);
        
        gbc.gridy = 2;
        panel.add(resetButton, gbc);
        
        resetButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (!email.isEmpty()) {
                UserAuthentication.initiatePasswordReset(email);
                JOptionPane.showMessageDialog(this,
                    "If the email exists in our system, you will receive reset instructions.",
                    "Password Reset",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        
        add(panel);
    }
}