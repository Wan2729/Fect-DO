package com.example.fectdo.models;

import java.util.List;

public class QuestionModel {
    List<String> questions, answerList, correctAnswer;
    int totalQuestions;

    public QuestionModel(List questions, List answerList, List correctAnswer, int totalQuestions) {
        this.questions = questions;
        this.answerList = answerList;
        this.correctAnswer = correctAnswer;
        this.totalQuestions = totalQuestions;
    }

    public String getQuestions(int index) {
        return questions.get(index);
    }

    public String[] getAnswerList(int index) {
        String answer = answerList.get(index);
        return answer.split(",");
    }

    public String getCorrectAnswer(int index) {
        return correctAnswer.get(index);
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }
}
