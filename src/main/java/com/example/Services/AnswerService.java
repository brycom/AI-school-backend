package com.example.Services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.Models.Answer;
import com.example.Models.Question;
import com.example.Models.Dtos.AiModelAnswer;
import com.example.Repositorys.AnswersRepository;
import com.example.Repositorys.QuestionRepository;
import java.util.UUID;

@Service
public class AnswerService {

    private final AnswersRepository answersRepository;
    private final QuestionRepository questionRepository;
    private final UserService userService;

    public AnswerService(AnswersRepository answersRepository, QuestionRepository questionRepository,
            UserService userService) {
        this.answersRepository = answersRepository;
        this.questionRepository = questionRepository;
        this.userService = userService;
    }

    public boolean evaluetAnswer(Answer answer, AiModelAnswer aiAnswer, String message) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = userService.getUserIdFromUsername(username);

        Question question = questionRepository.findByQuestionAndUser(message, userId);

        if (aiAnswer.isCorrect()) {
            question.setStatus(true);
            System.out.println("Rätt svar! Första frågan: " + question.getQuestion());
            questionRepository.save(question);
            answer.setQuestionId(question.getId());
            answersRepository.save(answer);

            return true;
        }

        return false;

    }

}
