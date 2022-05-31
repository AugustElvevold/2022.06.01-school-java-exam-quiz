package com.kristiania;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class QuizGame {
    Scanner userInput = new Scanner(System.in);

    String username;
    int userScore;
    String quizTopic;

    long newQuizStartTime;

    Connection con = new DatabaseConnection().con;

    public void start() {
        System.out.println("""
                \033[96m\033[1m
                
                
                       Hello and welcome to my quiz game!
                \033[0m""");
        setUsername();
    }

    private void setUsername() {
        System.out.println("\033[6mPlease enter a username and press Enter to apply");
        username = userInput.nextLine();
        while (!usernameIsValid()) {
            username = userInput.nextLine();
        }
        displayMainMenu();
    }
    private boolean usernameIsValid() {
        if (!username.matches("[a-zA-Z0-9]+")) {
            System.out.println("Please only use english letters and numbers!");
            return false;
        }
        else if (!(username.length() < 20)) {
            System.out.println("Name must be less than 20 characters");
            return false;
        }
        return true;
    }

    private void displayMainMenu() {
        if (username.isEmpty()) {
            setUsername();
        }
        System.out.println("""
                \033[96m\033[1m
                MAIN MENU\033[0m
                1. Start a new quiz
                2. Scoreboards
                3. Change username
                4. Quit
                
                Enter number for the action you want.
                \033[90m(You can also write "menu" or "quit" in console at any time to go back to main menu or quit the application.)\033[0m""");
        chooseMainMenuOption();
    }
    private String validateMainMenuChoice() {
        String[] validChoice = {"1", "2", "3", "4", "menu", "quit"};
        String userChoice = userInput.nextLine();
        while (Arrays.stream(validChoice).noneMatch(userChoice::equalsIgnoreCase)) {
            System.out.println("\033[31m'" + userChoice + "' is not an option. \nPlease choose one of the options 1, 2, 3 or 4\033[0m");
            userChoice = userInput.nextLine();
        }
        return userChoice;
    }
    private void chooseMainMenuOption() {
        switch (validateMainMenuChoice()) {
            case "1" -> chooseQuizTopic();
            case "2" -> scoreboardMenu();
            case "3" -> setUsername();
            case "4", "quit" -> {
                System.out.println("Program quiting..\nggs");
                System.exit(0);
            }
            case "menu" -> {
                System.out.println("You're already in the menu smartypants");
                chooseMainMenuOption();
            }
        }
    }

    // Displays scoreboard sub menu
    private void scoreboardMenu() {
        //TODO: make fancy
        System.out.println("""
                \033[96m\033[1m
                Scoreboard menu\033[0m
                1. Show all scores of a user
                2. Show my best scores
                3. Show highscores in "Minecraft" quiz
                4. Show highscores in "Rocket League" quiz
                5. Main menu
                6. Quit
                
                Enter number for the action you want.
                You can also write "menu" or "quit" in console at any time to go back to main menu or quit the application.
                """);
        String selectedOption = validateScoreboardMenuChoice();
        switch (selectedOption) {
            case "1" -> {
                System.out.println("What user do you want to see the scores of");
                listAllScoresForUser(userInput.nextLine());
            }
            case "2" -> listAllScoresForUser(username);
            case "3" -> minecraftQuizHighScores();
            case "4" -> rocketLeagueQuizHighScores();
            case "5", "menu" -> displayMainMenu();
            case "6", "quit" -> {
                System.out.println("Program quiting..\nggs");
                System.exit(0);
            }
        }
    }
    private String validateScoreboardMenuChoice() {
        String[] validChoice = {"1", "2", "3", "4", "5", "6", "menu", "quit"};
        String userChoice = userInput.nextLine();
        while (Arrays.stream(validChoice).noneMatch(userChoice::equalsIgnoreCase)) {
            System.out.println("\033[31m'" + userChoice + "' is not an option. \nPlease choose one of the options 1, 2, 3, 4, 5 or 6\033[0m");
            userChoice = userInput.nextLine();
        }
        return userChoice;
    }

    private void chooseQuizTopic() {
        // TODO: make text fancy
        System.out.println("""
                Quiz topics:
                
                1. Minecraft (Multiple choice)
                2. Rocket League (True or False)
                
                Enter number corresponding to preferred quiz:""");
        quizTopicChoice = validateChooseQuizTopicsChoice();
        startNewQuiz();
    }
    private String validateChooseQuizTopicsChoice() {
        String[] validChoice = {"1", "2", "menu", "quit"};
        String userChoice = userInput.nextLine();
        while (Arrays.stream(validChoice).noneMatch(userChoice::equalsIgnoreCase)) {
            System.out.println("\033[31m'" + userChoice + "' is not an option. \nPlease choose one either 1 or 2\033[0m");
            userChoice = userInput.nextLine();
        }
        return userChoice;
    }

    private void startNewQuiz() {
        userScore = 0;
        newQuizStartTime = new Date().getTime();
        System.out.println(newQuizStartTime);
        quizIsOngoing = true;
        switch (quizTopicChoice) {
            case "1" -> multiChoiceQuiz();
            case "2" -> binaryQuiz();
        }
    }

    // Runs multi choice quiz
    String quizTopicChoice;
    boolean quizIsOngoing = true;
    ArrayList<Quiz> questions;

    private void multiChoiceQuiz() {
        quizTopic = "Minecraft";
        int i = 0;
        questions = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM multichoicequiz")){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new MultiChoiceQuiz(rs.getString("question"),
                        rs.getString("correctAnswer"),
                        rs.getString("answerA"),
                        rs.getString("answerB"),
                        rs.getString("answerC"),
                        rs.getString("answerD")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (quizIsOngoing) {
            nextQuestion(i);
            String answer = validateMultiChoiceQuizAnswer();

            if (answer.equalsIgnoreCase(questions.get(i).correctAnswer)) {
                System.out.println("\033[92mYou are correct!\n\033[0m");
                userScore++;
            }
            else {
                switch (answer.toLowerCase()) {
                    case "menu" -> {displayMainMenu(); quizIsOngoing = false;}
                    case "quit" -> System.exit(0);
                    default -> System.out.println("\033[91mSorry, wrong answer\n\033[0m");
                }
            }
            i++;
            if (i >= questions.size()) quizIsOngoing = false;
        }

        // Makes a new score object with current username, score, topic, and the calculated time from start of quiz to now
        UserScore score = new UserScore(username, userScore, (int)(new Date().getTime() - newQuizStartTime) / 1000, quizTopic);
        addNewScoreToScoreboard(score);
        displayMinecraftScoreboard(score);
    }
    private String validateMultiChoiceQuizAnswer() {
        String[] validAnswers = {"A", "B", "C", "D", "menu", "quit"};
        String answer = userInput.nextLine();
        while (Arrays.stream(validAnswers).noneMatch(answer::equalsIgnoreCase)) {
            System.out.println("\033[31m'" + answer + "' is not an option. \nPlease choose one of the options A, B, C, D, menu or quit\033[0m");
            answer = userInput.nextLine();
        }
        return answer;
    }
    private void nextQuestion(int i) {
        System.out.println("Question " + (i + 1));
        questions.get(i).showQuestion();
    }

    private void iterateThroughQuestions() {

    }

    private void binaryQuiz() {
        quizTopic = "Rocket League";
        int i = 0;
        questions = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM binaryquiz")){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new BinaryQuiz(rs.getString("question"),
                        rs.getString("correctAnswer")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (quizIsOngoing) {
            nextQuestion(i);
            String answer = validateBinaryQuizAnswer();

            i++;
            if (i >= questions.size()) quizIsOngoing = false;
        }
        UserScore score = new UserScore(username, userScore, (int)(new Date().getTime() - newQuizStartTime) / 1000, quizTopic);
        addNewScoreToScoreboard(score);
        displayRocketLeagueScoreboard(score);
    }
    private String validateBinaryQuizAnswer() {
        String[] validAnswers = {"yes", "no", "menu", "quit"};
        String answer = userInput.nextLine();
        while (Arrays.stream(validAnswers).noneMatch(answer::equalsIgnoreCase)) {
            System.out.println("\033[31m'" + answer + "' is not an option. \nPlease answer with yes, no, menu or quit\033[0m");
            answer = userInput.nextLine();
        }
        return answer;
    }

    private void addNewScoreToScoreboard(UserScore newScore) {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO scoreboard (username, score, time, topic)" +
                    "VALUES (?, ?, ?, ?);"
            );
            stmt.setString(1, newScore.username());
            stmt.setInt(2, newScore.score());
            stmt.setInt(3, newScore.time());
            stmt.setString(4, newScore.topic());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayScoreBoard(UserScore highlightThisScore, String sql) {
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            System.out.format("\033[96m\033[1m%-20s %5s %5s", "Username", "Score", "Time\033[0m");
            while (rs.next()) {
                UserScore score = new UserScore(
                        rs.getString("username"),
                        rs.getInt("score"),
                        rs.getInt("time"),
                        rs.getString("topic"));
                if (score.username().equalsIgnoreCase(highlightThisScore.username())) {
                    System.out.format("\033[97m" + score + "\033[0m");
                }
                else System.out.println(score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void displayMinecraftScoreboard(UserScore highlightThisScore) {
        String sql = "SELECT * FROM scoreboard WHERE topic = 'Minecraft'";
        displayScoreBoard(highlightThisScore, sql);
    }
    private void displayRocketLeagueScoreboard(UserScore highlightThisScore) {
        String sql = "SELECT * FROM scoreboard WHERE topic = 'Rocket League'";
        displayScoreBoard(highlightThisScore, sql);

    }

    private void minecraftQuizHighScores() {
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM scoreboard WHERE topic = 'Minecraft' LIMIT 10")) {
            ResultSet rs = stmt.executeQuery();
            System.out.format("\033[96m\033[1m%-20s %5s %5s", "Username", "Score", "Time\033[0m");
            while (rs.next()) {
                System.out.println(new UserScore(
                        rs.getString("username"),
                        rs.getInt("score"),
                        rs.getInt("time"),
                        rs.getString("topic")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void rocketLeagueQuizHighScores() {
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM scoreboard WHERE topic = 'Rocket League' LIMIT 10")) {
            ResultSet rs = stmt.executeQuery();
            System.out.format("\033[96m\033[1m%-20s %5s %5s", "Username", "Score", "Time\033[0m");
            while (rs.next()) {
                System.out.println(new UserScore(
                        rs.getString("username"),
                        rs.getInt("score"),
                        rs.getInt("time"),
                        rs.getString("topic")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void listAllScoresForUser(String user) {
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM scoreboard WHERE username = ?")) {
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            System.out.format("\033[96m\033[1m%-20s %5s %5s %s", "Username", "Score", "Time", "Topic\033[0m");
            while (rs.next()) {
                System.out.println(new UserScore(
                        rs.getString("username"),
                        rs.getInt("score"),
                        rs.getInt("time"),
                        rs.getString("topic")).toStringWithTopic());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// TODO:
//      - Make menu work
//      - Add scoreboard to end of session
//      - Add quit game or go to main menu at any point
//      - Make fetching quiz one at the time or all at once
//      - Make questions random?