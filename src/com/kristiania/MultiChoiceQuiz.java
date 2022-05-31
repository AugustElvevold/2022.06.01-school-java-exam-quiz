package com.kristiania;

public class MultiChoiceQuiz extends Quiz {

    String answerA;
    String answerB;
    String answerC;
    String answerD;

    public MultiChoiceQuiz(String question, String correctAnswer, String answerA, String answerB, String answerC, String answerD) {
        super(question, correctAnswer);
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
    }

    @Override
    public void showQuestion() {
        System.out.println(super.question);
        System.out.println("A: " + answerA + "\t" +
                           "B: " + answerB + "\t" +
                           "C: " + answerC + "\t" +
                           "D: " + answerD);
    }

}