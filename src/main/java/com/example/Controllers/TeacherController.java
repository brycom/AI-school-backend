package com.example.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.Teacher;
import com.example.Models.Dtos.TeacherTopicDto;
import com.example.Services.TeacherService;

@RestController
@RequestMapping("/admin/teacher")
public class TeacherController {

    TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/newTeacher")
    public String newTeacher(@RequestBody Teacher teacher) {
        teacherService.newTeacher(teacher);
        return "Teacher created successfully";
    }

    @PostMapping("/topic")
    public String assignTopic(@RequestBody TeacherTopicDto input) {
        System.out.println(input.getTopic());
        System.out.println(input.getTeacher());
        teacherService.assignTopic(input.getTeacher(), input.getTopic());
        return "Topic assigned successfully";
    }

    @GetMapping("/allTeachers")
    public Iterable<Teacher> allTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/teacherByTopic/{topic}")
    public Iterable<Teacher> getTeachersByTopic(@PathVariable String topic) {
        return teacherService.getTeachersForTopic(topic);
    }

}
