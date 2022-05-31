package com.kristiania;

public class Main {
    public static void main(String[] args) {

        // Fills database with all questions and answers
        new CreateDatabase();

        QuizGame newGame = new QuizGame();
        newGame.start();
    }
}
