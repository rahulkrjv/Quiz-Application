// com.quizapp.model.User.java
package com.quizapp.model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String password;
    private int highScore;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.highScore = 0;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getHighScore() { return highScore; }
    public void setHighScore(int highScore) { this.highScore = highScore; }
}