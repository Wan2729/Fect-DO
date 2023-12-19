package com.example.fectdo.CourseDataBase;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class CourseModel {
    String courseName;
    List<String> ExamQuestion, ExamChoice, ExamAnswer;
    DocumentReference creatorID;
    List<DocumentReference> topics;

    public CourseModel() {
    }

    public CourseModel(String course_name) {
        this.courseName = course_name;
    }

    public CourseModel(String courseName, List<String> examQuestion, List<String> examChoice, List<String> examAnswer, DocumentReference creatorID, List<DocumentReference> topics) {
        this.courseName = courseName;
        ExamQuestion = examQuestion;
        ExamChoice = examChoice;
        ExamAnswer = examAnswer;
        this.creatorID = creatorID;
        this.topics = topics;
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

    public DocumentReference getCreatorID() {
        return creatorID;
    }

    public List<DocumentReference> getTopics() {
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

    public void setCreatorID(DocumentReference creatorID) {
        this.creatorID = creatorID;
    }

    public void setTopics(List<DocumentReference> topics) {
        this.topics = topics;
    }
}
