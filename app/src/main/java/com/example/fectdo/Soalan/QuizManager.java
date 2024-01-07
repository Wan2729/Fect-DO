package com.example.fectdo.Soalan;

import android.util.Log;

import com.example.fectdo.InternalStorageManager.TextFileManager;
import com.example.fectdo.course.Activity.AddCourse;

import java.util.ArrayList;
import java.util.List;

public class QuizManager {
    String topicName, videoLink;
    List<String> correctAnswer, question, choiceA, choiceB, choiceC, choiceD;
    List<String[]> choice;

    public QuizManager() {
        question = new ArrayList<>();
        correctAnswer = new ArrayList<>();
        choice = new ArrayList<>();
        choiceA = new ArrayList<>();
        choiceB = new ArrayList<>();
        choiceC = new ArrayList<>();
        choiceD = new ArrayList<>();
    }

    public void addQuestion(int index, String question, String correctAnswer, String[] choice){
        this.question.add(index,question);
        this.correctAnswer.add(index, correctAnswer);
        this.choice.add(index, choice);
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public void setCorrectAnswer(List<String> correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setCorrectAnswer(int index, String correctAnswer) {
        this.correctAnswer.set(index, correctAnswer);
    }

    public void addCorrectAnswer(int index){
        correctAnswer.add(index, "");
    }

    public void removeCorrectAnswer(int index){
        correctAnswer.remove(index);
    }

    public void setQuestion(List<String> question) {
        this.question = question;
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

    public void setChoice(List<String[]> choice) {
        this.choice = choice;
    }

    public List<String> getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getQuestion() {
        return question;
    }

    public List<String[]> getChoice() {
        return choice;
    }

    public String getCorrectAnswer(int index) {
        return correctAnswer.get(index);
    }

    public String getQuestion(int index) {
        return question.get(index);
    }

    public String[] getChoice(int index) {
        return choice.get(index);
    }

    public String getChoice(int index, int choice) {
        return this.choice.get(index)[choice];
    }

    public int getSize(){
        return question.size();
    }


    public String getTopicName() {
        return topicName;
    }

    public String getVideoLink() {
        return videoLink;
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

    public void loadChoice(){
        for(int i=0 ; i<choice.size() ; i++){
            choiceA.add(choice.get(i)[0]);
            choiceB.add(choice.get(i)[1]);
            choiceC.add(choice.get(i)[2]);
            choiceD.add(choice.get(i)[3]);
        }
    }
}
