package com.example.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    List<UUID> topic;

    public Teacher(String name, String description, List<UUID> topic) {
        this.name = name;
        this.description = description;
        this.topic = new ArrayList<UUID>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UUID> getTopic() {
        return topic;
    }

    public void setTopic(List<UUID> topic) {
        this.topic = topic;
    }

}