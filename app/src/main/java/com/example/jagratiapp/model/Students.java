package com.example.jagratiapp.model;

import java.io.StringReader;

public class Students {

    private String studentName;
    private String className;
    private String groupName;
    private String guardianName;
    private String mobileNo;
    private String villageName;


    public Students(String studentName, String className, String groupName, String guardianName, String mobileNo, String villageName) {
        this.studentName = studentName;
        this.className = className;
        this.groupName = groupName;
        this.guardianName = guardianName;
        this.mobileNo = mobileNo;
        this.villageName = villageName;
    }

    public Students(){}

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}
