package com.example.Models.Dtos;

import com.example.Models.Teacher;
import com.example.Models.Topic;

public class QuestionDto {

    private Teacher teacher;
    private Topic topic;

    public QuestionDto(Teacher teacher, Topic topic) {
        this.teacher = teacher;
        this.topic = topic;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

}
