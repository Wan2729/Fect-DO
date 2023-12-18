package com.example.fectdo.CourseDataBase;

public class CourseModel {
    String courseName;

    public CourseModel() {
    }

    public CourseModel(String course_name) {
        this.courseName = course_name;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
