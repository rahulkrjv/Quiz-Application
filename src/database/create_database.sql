-- Create the quiz database
CREATE DATABASE quiz_application;
USE quiz_application;

-- Categories table
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Difficulty levels table
CREATE TABLE difficulty_levels (
    difficulty_id INT PRIMARY KEY AUTO_INCREMENT,
    level_name VARCHAR(50) NOT NULL,
    description TEXT
);

-- Questions table
CREATE TABLE questions (
    question_id INT PRIMARY KEY AUTO_INCREMENT,
    category_id INT,
    difficulty_id INT,
    question_text TEXT NOT NULL,
    explanation TEXT,
    points INT DEFAULT 10,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    FOREIGN KEY (difficulty_id) REFERENCES difficulty_levels(difficulty_id)
);

-- Options table
CREATE TABLE options (
    option_id INT PRIMARY KEY AUTO_INCREMENT,
    question_id INT,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);

-- Question feedback table
CREATE TABLE question_feedback (
    feedback_id INT PRIMARY KEY AUTO_INCREMENT,
    question_id INT,
    user_id INT,
    feedback_text TEXT,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES questions(question_id)
);

-- Question tags table
CREATE TABLE tags (
    tag_id INT PRIMARY KEY AUTO_INCREMENT,
    tag_name VARCHAR(50) NOT NULL UNIQUE
);

-- Question-Tag relationship table
CREATE TABLE question_tags (
    question_id INT,
    tag_id INT,
    PRIMARY KEY (question_id, tag_id),
    FOREIGN KEY (question_id) REFERENCES questions(question_id),
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id)
);

-- Insert categories
INSERT INTO categories (category_name, description) VALUES
('Programming', 'Questions about programming concepts'),
('Mathematics', 'Mathematical problems and concepts'),
('General Knowledge', 'General knowledge questions');

-- Insert difficulty levels
INSERT INTO difficulty_levels (level_name, description) VALUES
('Easy', 'Basic level questions'),
('Medium', 'Intermediate level questions'),
('Hard', 'Advanced level questions');