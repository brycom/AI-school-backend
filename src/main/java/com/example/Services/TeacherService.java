package com.example.Services;

import java.security.PublicKey;

import org.springframework.stereotype.Service;

import com.example.Models.Teacher;
import com.example.Repositorys.TeacherRepository;

@Service
public class TeacherService {

    private TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher newTeacher(Teacher teacher) {
        if (teacherRepository.getTeacherByname(teacher.getName()) != null) {
            throw new IllegalArgumentException("Teacher with name " + teacher.getName() + " already exists");
        }
        return teacherRepository.save(teacher);

    }

    public void assignTopic(String teacher, String topic) {
        Teacher teacher2 = teacherRepository.getTeacherByname(teacher);
        if (teacher2 != null) {
            teacher2.addTopic(topic);
            teacherRepository.save(teacher2);
        }
    }

    public Iterable<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Iterable<Teacher> getTeachersForTopic(String topic) {
        return teacherRepository.findByTopic(topic);
    }

}
