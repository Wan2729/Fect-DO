package com.example.fectdo.Soalan;

import java.util.ArrayList;
import java.util.List;

public class QuizManager {
    List<String> correctAnswer, question;
    List<String[]> choice;

    public QuizManager() {
        question = new ArrayList<>();
        correctAnswer = new ArrayList<>();
        choice = new ArrayList<>();
    }

    public void addQuestion(int index, String question, String correctAnswer, String[] choice){
        this.question.add(index,question);
        this.correctAnswer.add(index, correctAnswer);
        this.choice.add(index, choice);
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
}
