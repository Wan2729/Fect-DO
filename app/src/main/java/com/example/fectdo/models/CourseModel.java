package com.example.fectdo.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class CourseModel {
    String courseName, creatorID, documentID;
    List<String> ExamQuestion, ExamChoice, ExamAnswer;
    List<String> topics;

    public CourseModel() {
    }

    public CourseModel(String course_name) {
        this.courseName = course_name;
    }

    public CourseModel(String courseName, List<String> examQuestion, List<String> examChoice, List<String> examAnswer, String creatorID, List<String> topics) {
        this.courseName = courseName;
        ExamQuestion = examQuestion;
        ExamChoice = examChoice;
        ExamAnswer = examAnswer;
        this.creatorID = creatorID;
        this.topics = topics;
    }

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getCourseName() {
        return courseName;
    }

    public List<String> getExamQuestion() {
        return ExamQuestion;
    }

    public List<String> getExamChoice() {
        return ExamChoice;
    }

    public List<String> getExamAnswer() {
        return ExamAnswer;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setExamQuestion(List<String> examQuestion) {
        ExamQuestion = examQuestion;
    }

    public void setExamChoice(List<String> examChoice) {
        ExamChoice = examChoice;
    }

    public void setExamAnswer(List<String> examAnswer) {
        ExamAnswer = examAnswer;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
