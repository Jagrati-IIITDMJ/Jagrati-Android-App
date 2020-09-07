package com.example.jagratiapp.model;

public class Quiz {
    String quizName;
    String quizDescription;
    String quizID;

    public String getQuizDescription() {
        return quizDescription;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
    }

    public Quiz(String quizName, String quizID, String quizDescription) {
        this.quizName = quizName;
        this.quizID = quizID;
        this.quizDescription = quizDescription;
    }

    public Quiz() {
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizID() {
        return quizID;
    }

    public void setQuizID(String quizID) {
        this.quizID = quizID;
    }
}
