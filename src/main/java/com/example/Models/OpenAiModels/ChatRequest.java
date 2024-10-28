package com.example.Models.OpenAiModels;

import java.util.ArrayList;
import java.util.List;

import com.example.Models.Teacher;

public class ChatRequest {

    private String model;
    private List<Message> messages;
    private int n;
    private Teacher teacher;
    private boolean stream;

    public ChatRequest(String model, String prompt, int n, Teacher teacher, boolean stream) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.teacher = teacher;
        this.messages.add(new Message("system",
                "Ditt namn är: " + this.teacher.getName() + " och du är " + this.teacher.getDescription()));
        this.messages.add(new Message("user", prompt));
        this.n = n;
        this.stream = stream;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

}
