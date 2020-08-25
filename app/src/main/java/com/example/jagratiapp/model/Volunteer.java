package com.example.jagratiapp.model;

import android.appwidget.AppWidgetProviderInfo;

public class Volunteer {

    private String username;
    private String email;
    private String branch;
    private String batch;
    private String contact_no;
    private String userId;

    public Volunteer(String email,String name,String id){
        this.email = email;
        this.username = name;
        this.userId = id;
    }

    public Volunteer(String username, String email, String branch, String batch, String contact_no, String userId) {
        this.username = username;
        this.email = email;
        this.branch = branch;
        this.batch = batch;
        this.contact_no = contact_no;
        this.userId = userId;
    }

    public Volunteer(){

    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getMailId() {
        return email;
    }

    public void setMailId(String mailId) {
        this.email = mailId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getContactNo() {
        return contact_no;
    }

    public void setContactNo(String contactNo) {
        this.contact_no = contactNo;
    }

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }
}
