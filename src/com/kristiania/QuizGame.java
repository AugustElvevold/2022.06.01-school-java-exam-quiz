package com.kristiania;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizGame {
    Scanner userInput = new Scanner(System.in);
    String username;
    String quizTopic;

    Connection con;
    public QuizGame() {
        con = new DatabaseConnection().con;
    }

    public void start() {
        welcomeScreen();
        getUsername();
        chooseQuizTopic();
        startNewQuiz();
    }

    private void welcomeScreen() {
        System.out.println("""
                Hello and welcome to my quiz game!
                
                Please enter a username and press Enter to get started:""");
    }

    private void getUsername() {
        username = userInput.nextLine();
        while (!username.matches("[a-zA-Z0-9]+")) {
            System.out.println("Please only use letters and numbers!");
            username = userInput.nextLine();
        }
        System.out.println("Hi, " + username + "!");
    }

    private void chooseQuizTopic() {
        System.out.println("""
                Here are the topics you can take a quiz in:
                
                1. Minecraft (Multiple choice)
                2. Rocket League (True or False)
                
                Enter number corresponding to preferred quiz:""");
        String quizTopicChoice = userInput.nextLine();
        while (!quizTopicChoice.matches("[12]")) {
            System.out.println("'" + quizTopicChoice + "' is not valid. Select '1' or '2' please");
            quizTopicChoice = userInput.nextLine();
        }
        switch (quizTopicChoice) {
            case "1" -> {quizTopic = "multichoicequiz";}
            case "2" -> {quizTopic = "binaryquiz";}
        }
    }
    //TODO: FINNISH THIS
    private void startNewQuiz() {
        UserScore userscore = new UserScore();
        ArrayList<String> questions = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM ?");
            stmt.setString(1, quizTopic);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
//                questions.add(rs.getString("question"));
//                System.out.println(rs.getString("question"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
