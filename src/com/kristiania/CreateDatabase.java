package com.kristiania;

import java.sql.*;
import java.util.ArrayList;

public class CreateDatabase {

    Connection con = new DatabaseConnection().con;
    ArrayList<Quiz> quizQuestions = new ArrayList<>();

    public CreateDatabase() {
        creatTables();
        emptyAllTables();
        addQuestionsToArrayList();
        insertQuestionsToTables();
    }

    private void creatTables() {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS multichoiceQuiz (" +
                    "id INT AUTO_INCREMENT NOT NULL," +
                    "question VARCHAR(255)," +
                    "answerA VARCHAR(255)," +
                    "answerB VARCHAR(255)," +
                    "answerC VARCHAR(255)," +
                    "answerD VARCHAR(255)," +
                    "correctAnswer VARCHAR(255)," +
                    "PRIMARY KEY (id)" +
                    ");"
            );
            stmt.executeUpdate();

            stmt = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS binaryQuiz (" +
                    "id INT AUTO_INCREMENT NOT NULL," +
                    "question VARCHAR(255)," +
                    "correctAnswer VARCHAR(255)," +
                    "PRIMARY KEY (id)" +
                    ");"
            );
            stmt.executeUpdate();

            stmt = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS scoreBoard (" +
                    "id INT AUTO_INCREMENT NOT NULL," +
                    "username VARCHAR(255)," +
                    "score INT," +
                    "time INT," +
                    "topic VARCHAR(255)," +
                    "PRIMARY KEY (id)" +
                    ");"
            );
            stmt.executeUpdate();

            System.out.println("\033[32mSuccessfully created all tables\033[0m");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\033[91mError while trying to create tables\033[0m");
            System.exit(69);
        }
    }

    private void emptyAllTables() {
        emptyBinaryQuizTable();
        emptyMultiChoiceQuizTable();
//        emptyScoreboardTable();
    }
    private void emptyBinaryQuizTable() {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM binaryquiz;"
            );
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void emptyMultiChoiceQuizTable() {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM multichoicequiz;"
            );
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Uncomment in emptyAllTables() method to easily empty scoreboard table when starting the Java program
    public void emptyScoreboardTable() {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM scoreboard;"
            );
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addQuestionsToArrayList() {
        quizQuestions.add(new MultiChoiceQuiz(
                "Which of these mobs can't drop a mob head item in survival?",
                "c",
                "Zombie",
                "Creeper",
                "Spider",
                "Skeleton"));
        quizQuestions.add(new MultiChoiceQuiz(
                "What color is a creeper?",
                "a",
                "Green",
                "Black",
                "Pink",
                "Blue"));
        quizQuestions.add(new MultiChoiceQuiz(
                "Whats the lowes level pickaxe you can mine diamonds with?",
                "b",
                "Wooden pickaxe",
                "Iron pickaxe",
                "Netherite pickaxe",
                "Stone pickaxe"));
        quizQuestions.add(new MultiChoiceQuiz(
                "Whats the seed of the world that was used in the first title screen panorama?",
                "d",
                "1985656462612183024",
                "1466403486068282115",
                "6866221629668753640",
                "2151901553968352745"));

        quizQuestions.add(new BinaryQuiz(
                "Rocket League is a shooter game taking place in space",
                "no"));
        quizQuestions.add(new BinaryQuiz(
                "Rule 1 in Rocket League is 'If you collide head-to-head or side-to-side with an opponent and become deadlocked, you must hold down the gas and wait for the deadlock to be resolved by other means.'",
                "yes"));
        quizQuestions.add(new BinaryQuiz(
                "The maker of this quiz have played Rocket League",
                "yes"));
        quizQuestions.add(new BinaryQuiz(
                "The highest rank you can get is 'Grand champ'",
                "no"));
    }
    private void insertQuestionsToTables() {
        for (Quiz object : quizQuestions) {
            if (object instanceof MultiChoiceQuiz multiChoiceQuiz) {
                addMultiChoiceQuestion(multiChoiceQuiz);
            }
            else if (object instanceof BinaryQuiz binaryQuiz) {
                addBinaryQuizQuestion(binaryQuiz);
            }
        }
        System.out.println("\033[32mAdded questions to tables\033[0m");
    }

    private void addMultiChoiceQuestion(MultiChoiceQuiz questionObject) {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO multichoiceQuiz (question, answerA, answerB, answerC, answerD, correctAnswer)" +
                    "VALUES (?, ?, ?, ?, ?, ?);"
            );
            stmt.setString(1, questionObject.question);
            stmt.setString(2, questionObject.answerA);
            stmt.setString(3, questionObject.answerB);
            stmt.setString(4, questionObject.answerC);
            stmt.setString(5, questionObject.answerD);
            stmt.setString(6, questionObject.correctAnswer);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addBinaryQuizQuestion(BinaryQuiz questionObject) {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO binaryquiz (question, correctAnswer)" +
                    "VALUES (?, ?);"
            );
            stmt.setString(1, questionObject.question);
            stmt.setString(2, questionObject.correctAnswer);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
