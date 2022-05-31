package com.kristiania;

public abstract class Quiz {
    String question;
    String correctAnswer;

    public Quiz(String question, String correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    public abstract void showQuestion();

    public boolean isCorrectAnswer(String answer) {
        return answer.equalsIgnoreCase(correctAnswer);
    }
}