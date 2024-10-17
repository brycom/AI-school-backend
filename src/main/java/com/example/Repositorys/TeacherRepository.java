package com.example.Repositorys;

import org.springframework.data.repository.CrudRepository;

import com.example.Models.Teacher;

public interface TeacherRepository extends CrudRepository<Teacher, Integer> {

    Teacher getTeacherByname(String name);

}
