package com.example.Models.Dtos;

public class TeacherTopicDto {

    private String topic;
    private String teacher;

    public TeacherTopicDto(String topic, String teacher) {
        this.topic = topic;
        this.teacher = teacher;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

}
