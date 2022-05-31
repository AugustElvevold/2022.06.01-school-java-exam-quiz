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
        System.out.println("""
                \033[96m\033[1m
                MAIN MENU\033[0m\t\t\t\033[90m\033[3mUsername: \033[36m""" + username + """
                \033[0m
                1. Start a new quiz
                2. Scoreboards
                3. Change username
                4. Quit
                
                Enter number in console, for the corresponding action you want
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
    private void escapeToMainMenu() {
        System.out.println("\033[90mEnter any normal key to go to main menu\033[0m");
        userInput.nextLine();
        displayMainMenu();
    }

    /** Displays menu to choose quiz topic */
    private void chooseQuizTopic() {
        System.out.println("""
                \033[96m\033[1m
                Quiz topics\033[0m
                1. Minecraft (Multiple choice)
                2. Rocket League (True or False)
                \033[90m(Enter number corresponding to preferred quiz)\033[0m""");
        startNewQuiz(validateChooseQuizTopicsChoice());
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

    /** Displays scoreboard sub menu */
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

    private void startNewQuiz(String quizTopicChoice) {
        userScore = 0;
        newQuizStartTime = new Date().getTime();
        switch (quizTopicChoice) {
            case "1" -> multiChoiceQuiz();
            case "2" -> binaryQuiz();
            case "menu" -> displayMainMenu();
            case "quit" -> {
                    System.out.println("Program quiting..\nggs");
                    System.exit(0);
            }
        }
    }

    /** Runs multi choice quiz */
    private void multiChoiceQuiz() {
        quizTopic = "Minecraft";
        System.out.println(quizTopic);
        ArrayList<Quiz> questions = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM multichoicequiz");
            ResultSet rs = stmt.executeQuery()){
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

        String[] validAnswers = {"A", "B", "C", "D", "menu", "quit"};
        iterateThroughQuestions(questions, validAnswers);

        quizFinished();
    }

    /** Runs binary quiz */
    private void binaryQuiz() {
        quizTopic = "Rocket League";
        ArrayList<Quiz> questions = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM binaryquiz")){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new BinaryQuiz(rs.getString("question"),
                        rs.getString("correctAnswer")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] validAnswers = {"yes", "no", "menu", "quit"};
        iterateThroughQuestions(questions, validAnswers);

        quizFinished();
    }

    /** Makes a new score object with current username, score, topic, and the calculated time from start of quiz to now, and adds it to the database */
    private void quizFinished() {
        UserScore score = new UserScore(username, userScore, (int)(new Date().getTime() - newQuizStartTime) / 1000, quizTopic);
        addNewScoreToScoreboard(score);
        switch (quizTopic) {
            case "Minecraft" -> displayMinecraftScoreboard(score);
            case "Rocket League" -> displayRocketLeagueScoreboard(score);
        }
    }

    private void iterateThroughQuestions(ArrayList<Quiz> questions, String[] validAnswers) {
        int i = 1;
        for (Quiz question : questions) {
            System.out.println("\nQuestion " + i);
            question.showQuestion();
            String answer = validateQuizAnswer(validAnswers);

            if (answer.equalsIgnoreCase(question.correctAnswer)) {
                System.out.println("\033[92mYou are correct!\033[0m");
                userScore++;
            }
            else {
                switch (answer.toLowerCase()) {
                    case "menu" -> displayMainMenu();
                    case "quit" -> System.exit(0);
                    default -> System.out.println("\033[91mSorry, wrong answer\033[0m");
                }
            }
            i++;
        }
    }

    private String validateQuizAnswer(String[] validAnswers) {
        String answer = userInput.nextLine();
        while (Arrays.stream(validAnswers).noneMatch(answer::equalsIgnoreCase)) {
            if (quizTopic.equals("Minecraft")) {
                System.out.println("\033[31m'" + answer + "' is not an option. \nPlease choose one of the options A, B, C, D, menu or quit\033[0m");
            }
            else if (quizTopic.equals("Rocket League")) {
                System.out.println("\033[31m'" + answer + "' is not an option. \nPlease answer with yes, no, menu or quit\033[0m");
            }
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
            System.out.println("\n\033[96m\033[1mAll " + quizTopic + " scores\033[0m");
            System.out.format("\033[36m\033[1m%-20s %5s %4s %s", "Username", "Score", "Time", "\033[0m\n");
            while (rs.next()) {
                UserScore score = new UserScore(
                        rs.getString("username"),
                        rs.getInt("score"),
                        rs.getInt("time"),
                        rs.getString("topic"));
                if (score.username().equals(highlightThisScore.username()) &&
                    score.score() == (highlightThisScore.score()) &&
                    score.time() == (highlightThisScore.time())
                ) {
                    System.out.format("\033[97m\033[1m" + score + "\033[0m\n");
                }
                else System.out.println(score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        escapeToMainMenu();
    }
    private void displayMinecraftScoreboard(UserScore highlightThisScore) {
        String sql = "SELECT * FROM scoreboard WHERE topic = 'Minecraft' ORDER BY score DESC, time ASC";
        displayScoreBoard(highlightThisScore, sql);
    }
    private void displayRocketLeagueScoreboard(UserScore highlightThisScore) {
        String sql = "SELECT * FROM scoreboard WHERE topic = 'Rocket League' ORDER BY score DESC, time ASC";
        displayScoreBoard(highlightThisScore, sql);

    }

    private void minecraftQuizHighScores() {
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM scoreboard WHERE topic = 'Minecraft' ORDER BY score DESC, time LIMIT 10")) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("\n\033[96m\033[1mTop 10 Minecraft scores\033[0m");
            System.out.format("\033[36m\033[1m%-20s %5s %6s %s", "Username", "Score", "Time", "\n\033[0m");
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
        escapeToMainMenu();
    }
    private void rocketLeagueQuizHighScores() {
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM scoreboard WHERE topic = 'Rocket League' ORDER BY score DESC, time LIMIT 10")) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("\n\033[96m\033[1mTop 10 Rocket League scores\033[0m");
            System.out.format("\033[36m\033[1m%-20s %5s %6s %s", "Username", "Score", "Time", "\n\033[0m");
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
        escapeToMainMenu();
    }

    private void listAllScoresForUser(String user) {
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM scoreboard WHERE username = ? ORDER BY Topic, score DESC, time")) {
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            System.out.println("\n\033[96m\033[1mAll scores for \033[0m\033[36m\033[3m" + user + "\033[0m");
            System.out.format("\033[36m\033[1m%-20s %5s %6s %s", "Username", "Score", "Time", "Topic\n\033[0m");
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
        escapeToMainMenu();
    }
}

// TODO:
//      - Make menu work
//      - Add scoreboard to end of session
//      - Add quit game or go to main menu at any point
//      - Make fetching quiz one at the time or all at once
//      - Make questions random?