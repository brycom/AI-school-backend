package com.example.Models.OpenAiModels;

import java.util.List;

import com.example.Models.Question;
import com.example.Models.Teacher;

public class ChatAnswer {

    private String model;
    private List<Message> messages;
    private int n;
    private Teacher teacher;
    private Question question;

    public ChatAnswer(String model, List<Message> messages, int n, Teacher teacher, Question question) {
        this.model = model;
        this.messages = messages;
        this.n = n;
        this.teacher = teacher;
        this.question = question;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
