// com.quizapp.util.UserAuthentication.java
package com.quizapp.util;

import com.quizapp.model.User;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserAuthentication {
    private static final String USER_FILE = "users.dat";
    private static Map<String, User> users = new HashMap<>();
    
    static {
        loadUsers();
    }

    public static void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            users = (HashMap<String, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing users file found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean registerUser(String name, String email, String password) {
        if (users.containsKey(email)) {
            return false; // User already exists
        }

        User newUser = new User(name, email, password);
        users.put(email, newUser);
        saveUsers();
        return true;
    }

    public static User loginUser(String email, String password) {
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public static boolean isEmailRegistered(String email) {
        return users.containsKey(email);
    }

    private static Map<String, PasswordResetToken> resetTokens = new HashMap<>();

    static class PasswordResetToken {
        String token;
        long expiryTime;
        String email;

        PasswordResetToken(String token, String email) {
            this.token = token;
            this.email = email;
            this.expiryTime = System.currentTimeMillis() + (30 * 60 * 1000); // 30 minutes
        }

        boolean isValid() {
            return System.currentTimeMillis() < expiryTime;
        }
    }

    public static void initiatePasswordReset(String email) {
        if (users.containsKey(email)) {
            String resetToken = SecurityUtil.generateSecureToken();
            resetTokens.put(resetToken, new PasswordResetToken(resetToken, email));
            EmailService.sendPasswordResetEmail(email, resetToken);
        }
    }

    public static boolean resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = resetTokens.get(token);
        if (resetToken != null && resetToken.isValid()) {
            User user = users.get(resetToken.email);
            user.setPassword(SecurityUtil.hashPassword(newPassword));
            resetTokens.remove(token);
            saveUsers();
            return true;
        }
        return false;
    }
}