package com.example.jagratiapp.model;

import java.util.Map;

public class QuizReport {
   private Map<String,String> answerList;
   private int marks;

    public QuizReport(Map<String, String> answerList, int marks) {
        this.answerList = answerList;
        this.marks = marks;
    }

    public QuizReport() {
    }

    public Map<String, String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(Map<String, String> answerList) {
        this.answerList = answerList;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
