```markdown
# Quiz Application

A Java Swing-based interactive quiz application featuring multiple-choice questions, user authentication, and performance analytics.

## Quick Features
- 📝 Multiple choice questions
- 🔐 User authentication (Email & Google)
- 📊 Performance statistics
- 📈 Visual analytics
- 🏆 Leaderboard system
- ⏱️ Timed quizzes

## Quick Setup

### Prerequisites
- Java JDK 8+
- MySQL 5.7+
- Maven 3.6+

### 1. Clone & Build
```bash
git clone https://github.com/yourusername/quiz-application.git
cd quiz-application
mvn install
```

### 2. Database Setup
```sql
CREATE DATABASE quiz_application;
mysql -u root -p quiz_application < database/schema.sql
```

### 3. Configure
Create `resources/database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/quiz_application
db.user=your_username
db.password=your_password
```

### 4. Run
```bash
java -jar target/quiz-application-1.0-SNAPSHOT.jar
```

## Project Structure
```
src/
├── com.quizapp/
    ├── main/      # Main application
    ├── gui/       # UI components
    ├── model/     # Data models
    ├── util/      # Utilities
    └── dao/       # Data access
```

## Quick Start Guide

1. **Login/Register**
   - Use email/password
   - Or click "Login with Google"

2. **Take Quiz**
   - Select category
   - Answer questions
   - View instant feedback

3. **View Statistics**
   - Check performance
   - View progress charts
   - Compare scores

## Google Auth Setup

1. Get credentials from [Google Cloud Console](https://console.cloud.google.com/)
2. Add to `resources/google_oauth.properties`:
```properties
google.client.id=your-client-id
google.client.secret=your-client-secret
google.redirect.uri=http://localhost:8888/callback
```

## Screenshots
[Add your application screenshots here]

## Need Help?
- Check [Documentation](docs/)
- Create an [Issue](issues/)
- Email: support@quizapp.com

## License
MIT License
```
