package com.example.jagratiapp.model;

public class BugReport {
    private String description;
    private String username;
    private String timeStamp;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public BugReport(String description, String timeStamp, String username) {
        this.description = description;
        this.timeStamp = timeStamp;
        this.username = username;
    }



    public BugReport() {
    }
}
