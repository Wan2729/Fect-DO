package com.example.fectdo.models;

public class EnrollmentModel {
    private String userID, courseID;

    public EnrollmentModel() {
    }

    public EnrollmentModel(String userID, String courseID) {
        this.userID = userID;
        this.courseID = courseID;
    }

    public String getUserID() {
        return userID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }
}
