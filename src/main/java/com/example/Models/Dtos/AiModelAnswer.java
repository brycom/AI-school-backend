package com.example.Models.Dtos;

public class AiModelAnswer {

    private boolean correct;
    private String feedback;

    public AiModelAnswer(boolean correct, String feedback) {
        this.correct = correct;
        this.feedback = feedback;
    }

    public AiModelAnswer() {
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

}
