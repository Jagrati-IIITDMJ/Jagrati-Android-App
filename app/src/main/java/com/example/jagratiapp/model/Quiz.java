package com.example.jagratiapp.model;

public class Quiz {
    private String quizName;
    private String quizDescription;
    private String quizID;
    private int numberOfQues;
    private int quesTime;
    private long marks;
    private boolean quizState = false;

    public long getMarks() {
        return marks;
    }

    public void setMarks(long marks) {
        this.marks = marks;
    }

    public int getQuesTime() {
        return quesTime;
    }

    public void setQuesTime(int quesTime) {
        this.quesTime = quesTime;
    }

    public int getNumberOfQues() {
        return numberOfQues;
    }

    public void setNumberOfQues(int numberOfQues) {
        this.numberOfQues = numberOfQues;
    }

    public String getQuizDescription() {
        return quizDescription;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
    }

    public Quiz(String quizName, String quizDescription,int quizTime) {
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.quesTime = quizTime;
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

    public boolean isQuizState() {
        return quizState;
    }

    public void setQuizState(boolean quizState) {
        this.quizState = quizState;
    }
}
