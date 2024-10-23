package com.example.Models.Dtos;

import com.example.Models.Answer;
import com.example.Models.Question;
import com.example.Models.Teacher;

public class AnswerDto {
    private Question question;
    private Answer answer;
    private Teacher teacher;

    public AnswerDto(Question question, Answer answer, Teacher teacher) {
        this.question = question;
        this.answer = answer;
        this.teacher = teacher;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
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
