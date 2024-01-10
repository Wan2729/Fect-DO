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

    public void addQuestion(int index){
        this.question.add(index,"");
        this.correctAnswer.add(index, "");
        this.choice.add(index, new String[4]);
        choiceA.add(index, "");
        choiceB.add(index, "");
        choiceC.add(index, "");
        choiceD.add(index, "");
    }

    public void removeQuestion(int index){
        this.question.remove(index);
        this.correctAnswer.remove(index);
        this.choice.remove(index);
        choiceA.remove(index);
        choiceB.remove(index);
        choiceC.remove(index);
        choiceD.remove(index);
    }

    public void setQuestion(int index, String question, String[] choice){
        this.question.set(index,question);
        this.choice.set(index, choice);
        choiceA.set(index, choice[0]);
        choiceB.set(index, choice[1]);
        choiceC.set(index, choice[2]);
        choiceD.set(index, choice[3]);
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

    public void addQuestions(List<String> questions) {
        question.addAll(questions);
    }

    public void addCorrectAnswer(List<String> correctAnswers){
        correctAnswer.addAll(correctAnswers);
    }

    public void addChoiceA(List<String> choices){
        choiceA.addAll(choices);
    }

    public void addChoiceB(List<String> choices){
        choiceB.addAll(choices);
    }

    public void addChoiceC(List<String> choices){
        choiceC.addAll(choices);
    }

    public void addChoiceD(List<String> choices){
        choiceD.addAll(choices);
    }
}
