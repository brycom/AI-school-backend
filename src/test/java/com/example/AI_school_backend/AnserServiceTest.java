package com.example.AI_school_backend;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.Models.Answer;
import com.example.Models.Question;
import com.example.Models.Dtos.AiModelAnswer;
import com.example.Repositorys.AnswersRepository;
import com.example.Repositorys.QuestionRepository;
import com.example.Services.AnswerService;
import com.example.Services.UserService;

import java.util.UUID;

public class AnserServiceTest {

    @InjectMocks
    private AnswerService answerService;

    @Mock
    private UserService userService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private AnswersRepository answersRepository;

    private final String username = "testUser";
    private final UUID userId = UUID.randomUUID();
    private Question question;
    String message = "Sample question";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn(username);
        when(userService.getUserIdFromUsername(username)).thenReturn(userId);

        question = new Question(UUID.randomUUID(), userId, message, false);
        when(questionRepository.findByQuestionAndUser(message, userId)).thenReturn(question);
    }

    @Test
    public void testEvaluateAnswerFalse() {
        AiModelAnswer aiAnswer = new AiModelAnswer(false, null);

        boolean result = answerService.evaluetAnswer(new Answer(), aiAnswer, message);

        assertFalse(result, "Expected result to be false when AI answer is incorrect.");
        assertFalse(question.isStatus(), "Expected question status to remain false.");
    }

    @Test
    public void testEvaluateAnswerTrue() {
        AiModelAnswer aiAnswer = new AiModelAnswer(true, null);

        boolean result = answerService.evaluetAnswer(new Answer(), aiAnswer, message);

        assertTrue(result, "Expected result to be true when AI answer is correct.");
        assertTrue(question.isStatus(), "Expected question status to be true after correct answer.");
    }
}
