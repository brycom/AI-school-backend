package com.example.Services;

import org.springframework.stereotype.Service;

import com.example.Models.Question;
import com.example.Models.Dtos.AiModelAnswer;
import com.example.Repositorys.AnswersRepository;
import com.example.Repositorys.QuestionRepository;

@Service
public class AnswerService {

    private final AnswersRepository answersRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(AnswersRepository answersRepository, QuestionRepository questionRepository) {
        this.answersRepository = answersRepository;
        this.questionRepository = questionRepository;
    }

    public boolean evaluetAnswer(AiModelAnswer answer, String message) {

        Question question = questionRepository.findByQuestion(message);

        if (answer.isCorrect()) {
            question.setStatus(true);
            System.out.println("Rätt svar! Första frågan: " + question.getQuestion());
            questionRepository.save(question);

            return true;
        }

        return false;

    }

}
