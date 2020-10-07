package com.example.jagratiapp.student.Util;

public class StudentAPI {

    private String studentName;
    private String GuardianName;
    private String Rollno;
    private String villageName;
    private String classUid;
    private String groupUid;
    private String MobileNo;

    private static StudentAPI instance;

    public static StudentAPI Instance(){
        if (instance == null)
            instance = new StudentAPI();
        return instance;
    }

    private StudentAPI() {
    }


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGuardianName() {
        return GuardianName;
    }

    public void setGuardianName(String guardianName) {
        GuardianName = guardianName;
    }

    public String getRollno() {
        return Rollno;
    }

    public void setRollno(String rollno) {
        Rollno = rollno;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getClassUid() {
        return classUid;
    }

    public void setClassUid(String classUid) {
        this.classUid = classUid;
    }

    public String getGroupUid() {
        return groupUid;
    }

    public void setGroupUid(String groupUid) {
        this.groupUid = groupUid;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
