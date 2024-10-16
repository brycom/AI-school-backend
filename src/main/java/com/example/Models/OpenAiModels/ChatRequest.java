package com.example.Models.OpenAiModels;

import java.util.ArrayList;
import java.util.List;

public class ChatRequest {

    private String model;
    private List<Message> messages;
    private int n;
    /* private List<Message> previeusMessages ; */

    public ChatRequest(String model, String prompt, int n) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("system",
                "du är matte lärare ge antingen rätt eller fel om du får ett svar"));
        this.messages.add(new Message("user", prompt));
        this.messages.add(new Message("assistant", "Beräkna summan av 58 och 26."));
        this.n = n;
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

}
