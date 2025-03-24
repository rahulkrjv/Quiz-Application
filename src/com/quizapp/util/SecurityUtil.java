// com.quizapp.util.SecurityUtil.java
package com.quizapp.util;

import org.mindrot.jbcrypt.BCrypt;
import java.security.SecureRandom;
import java.util.regex.Pattern;

public class SecurityUtil {
    private static final int WORKLOAD = 12; // BCrypt workload factor
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // Password hashing
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(WORKLOAD));
    }

    // Password verification
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    // Generate secure token for password reset
    public static String generateSecureToken() {
        byte[] token = new byte[32];
        SECURE_RANDOM.nextBytes(token);
        return bytesToHex(token);
    }

    // Password strength validation
    public static boolean isPasswordStrong(String password) {
        if (password.length() < 8) return false;
        
        boolean hasUpper = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLower = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasNumber = Pattern.compile("\\d").matcher(password).find();
        boolean hasSpecial = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find();
        
        return hasUpper && hasLower && hasNumber && hasSpecial;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}