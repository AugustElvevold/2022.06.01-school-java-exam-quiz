package com.kristiania;

public class BinaryQuiz extends Quiz {

    public BinaryQuiz(String question, String correctAnswer) {
        super(question, correctAnswer);
    }

    @Override
    public void showQuestion() {
        System.out.println(super.question);
        System.out.println("Answer yes or no");
    }

}
