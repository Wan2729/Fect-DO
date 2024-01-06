package com.example.fectdo.models;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class TopicModel {
    private String topicName, videoLink;
    private String documentId;
    private List<String> question, correctAnswer, choiceA, choiceB, choiceC, choiceD;
    private int topicNum;

    public TopicModel() {
    }

    public TopicModel(String topicName, String videoLink) {
        this.topicName = topicName;
        this.videoLink = videoLink;
    }
    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public int getTopicNum() {
        return topicNum;
    }

    public List<String> getQuestion() {
        return question;
    }

    public List<String> getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getChoiceA() {
        return choiceA;
    }

    public List<String> getChoiceB() {
        return choiceB;
    }

    public List<String> getChoiceC() {
        return choiceC;
    }

    public List<String> getChoiceD() {
        return choiceD;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setTopicNum(int topicNum) {
        this.topicNum = topicNum;
    }

    public void setQuestion(List<String> question) {
        this.question = question;
    }

    public void setCorrectAnswer(List<String> correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setChoiceA(List<String> choiceA) {
        this.choiceA = choiceA;
    }

    public void setChoiceB(List<String> choiceB) {
        this.choiceB = choiceB;
    }

    public void setChoiceC(List<String> choiceC) {
        this.choiceC = choiceC;
    }

    public void setChoiceD(List<String> choiceD) {
        this.choiceD = choiceD;
    }
}
