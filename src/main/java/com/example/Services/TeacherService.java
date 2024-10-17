package com.example.Services;

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

}
