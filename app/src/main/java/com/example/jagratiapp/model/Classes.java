package com.example.jagratiapp.model;

public class Classes {
    private String className;
    private String uId;

    public Classes(){}

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Classes(String name){
        this.className = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
