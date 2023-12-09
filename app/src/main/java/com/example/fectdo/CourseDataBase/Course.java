package com.example.fectdo.CourseDataBase;

public class Course {
    String course_name;

    public Course() {
    }

    public Course(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
}
