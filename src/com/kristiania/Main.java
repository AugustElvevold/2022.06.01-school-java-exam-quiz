package com.kristiania;

public class Main {
    public static void main(String[] args) {

        // NOTE
        // RUN "Create user and database.sql" BEFORE RUNNIN MAIN

        // Fills database with all questions and answers
        new CreateDatabase();

        QuizGame newGame = new QuizGame();
        newGame.start();
    }
}
