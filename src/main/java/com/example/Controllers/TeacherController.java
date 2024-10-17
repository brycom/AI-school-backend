package com.example.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.Teacher;
import com.example.Services.TeacherService;

@RestController
@RequestMapping("/admin/teacher")
public class TeacherController {

    TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("newTeacher")
    public String newTeacher(@RequestBody Teacher teacher) {
        teacherService.newTeacher(teacher);
        return "Teacher created successfully";
    }

}
