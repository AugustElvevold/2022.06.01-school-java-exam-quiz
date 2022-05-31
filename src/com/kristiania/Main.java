package com.kristiania;

import javax.security.auth.login.CredentialException;

public class Main {
    public static void main(String[] args) {

        new CreateDatabase();

        QuizGame newGame = new QuizGame();
        newGame.start();
    }
}
