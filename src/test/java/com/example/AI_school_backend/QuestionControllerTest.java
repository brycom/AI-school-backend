package com.example.AI_school_backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import com.example.Controllers.QuestionController;
import com.example.Models.Question;
import com.example.Services.QuestionService;
import com.example.Services.UserService;

@SpringBootTest
public class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private QuestionController questionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetLastTenQuestions() {
        // Arrange
        String topic = UUID.randomUUID().toString();
        String username = "testUser";
        UUID userId = UUID.randomUUID();
        UUID topicId = UUID.fromString(topic);

        List<Question> questions = new ArrayList<Question>();
        for (int i = 0; i < 10; i++) {
            Question question = new Question();
            question.setQuestion("Question " + (i + 1));
            questions.add(question);
        }

        when(authentication.getName()).thenReturn(username);
        when(userService.getUserIdFromUsername(username)).thenReturn(userId);
        when(questionService.getLastTenQuestions(userId, topicId)).thenReturn(questions);

        // Act
        List<Question> result = questionController.getLastTenQuestions(topic);

        // Assert
        assertNotNull(result, "Expected non-null result");
        assertTrue(result.size() <= 10, "Expected result size to be less than or equal to 10");
        assertEquals(10, result.size(), "Expected result size to be 10");
    }

}
