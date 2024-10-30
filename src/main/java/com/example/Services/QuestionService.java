package com.example.Services;

import org.springframework.stereotype.Service;

import com.example.Models.Question;
import com.example.Repositorys.QuestionRepository;

import java.util.List;

import java.util.UUID;

@Service
public class QuestionService {

    QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getLastTenQuestions(UUID userId, UUID topicId) {
        List<Question> questions = questionRepository.findAllByUserIdAndTopicId(userId, topicId);
        if (questions.size() > 10) {
            return questions.subList(questions.size() - 10, questions.size());
        }
        return questions;
    }

}
