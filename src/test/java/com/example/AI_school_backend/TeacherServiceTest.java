package com.example.AI_school_backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.Models.Teacher;
import com.example.Repositorys.TeacherRepository;
import com.example.Services.TeacherService;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testNewTeacherSuccess() {
        Teacher teacher = new Teacher();
        teacher.setName("John Doe");

        when(teacherRepository.getTeacherByname("John Doe")).thenReturn(null);
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        Teacher newTeacher = teacherService.newTeacher(teacher);

        assertEquals(teacher.getName(), newTeacher.getName(), "Expected the teacher to be saved");
        verify(teacherRepository).save(teacher);
    }

    @Test
    public void testNewTeacherAlreadyExists() {
        Teacher teacher = new Teacher();
        teacher.setName("John Doe");

        when(teacherRepository.getTeacherByname("John Doe")).thenReturn(teacher);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            teacherService.newTeacher(teacher);
        });

        assertEquals("Teacher with name John Doe already exists", exception.getMessage());
        verify(teacherRepository, never()).save(teacher);
    }

    @Test
    public void testAssignTopicSuccess() {

        Teacher teacher = new Teacher();
        teacher.setName("John Doe");

        when(teacherRepository.getTeacherByname("John Doe")).thenReturn(teacher);

        teacherService.assignTopic("John Doe", "Math");

        verify(teacherRepository).save(teacher);
        assertTrue(teacher.getTopic().contains("Math"), "Expected the topic to be assigned to the teacher");
    }

    @Test
    public void testAssignTopicTeacherNotFound() {

        when(teacherRepository.getTeacherByname("John Doe")).thenReturn(null);

        teacherService.assignTopic("John Doe", "Math");

        verify(teacherRepository, never()).save(any());
    }

    @Test
    public void testGetAllTeachers() {
        int size = 0;
        List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
        when(teacherRepository.findAll()).thenReturn(teachers);

        Iterable<Teacher> result = teacherService.getAllTeachers();
        for (Teacher r : result) {
            size++;
        }

        assertEquals(teachers.size(), size, "Expected all teachers to be returned");
    }

}
