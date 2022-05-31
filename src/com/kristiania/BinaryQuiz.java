package com.kristiania;

public class BinaryQuiz extends Quiz {

    public BinaryQuiz(String question, String correctAnswer) {
        super(question, correctAnswer);
    }

    @Override
    public void showQuestion() {
        System.out.println("\033[1m" + super.question + "\033[0m");
        System.out.println("\033[90mAnswer yes or no\033[0m");
    }

}
