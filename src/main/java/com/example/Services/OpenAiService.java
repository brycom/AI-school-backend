package com.example.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Models.Answer;
import com.example.Models.Question;
import com.example.Models.Teacher;
import com.example.Models.Topic;
import com.example.Models.OpenAiModels.ChatRequest;
import com.example.Models.OpenAiModels.ChatRespons;
import com.example.Models.OpenAiModels.Message;
import com.example.Repositorys.QuestionRepository;

import java.util.UUID;

@Service
public class OpenAiService {

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    private final RestTemplate restTemplate;
    private final QuestionRepository questionRepository;
    private UserService userService;

    public OpenAiService(RestTemplate restTemplate, UserService userService, QuestionRepository questionRepository) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.questionRepository = questionRepository;
    }

    public ChatRespons sendQuestion(Topic topic, Teacher teacher) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = userService.getUserIdFromUsername(username);
        ChatRequest chatRequest = new ChatRequest("gpt-4o",
                "Generera en " + topic.getTopic() + " fråga om " + topic.getDescription() + "med en svårighetsgrad på:"
                        + topic.getLevel() + "av 10. Frågan ska vara kort och inget svar ska ges ",
                1,
                teacher);
        ChatRespons respons = restTemplate.postForObject(openAiApiUrl, chatRequest, ChatRespons.class);
        Question question = new Question(topic.getId(), userId, respons.getChoices().get(0).getMessage().getContent(),
                false);
        questionRepository.save(question);

        return respons;
    }

    public ChatRespons sendAnswer(Answer answer, Question question, Teacher teacher) {
        ChatRequest chatRequest = new ChatRequest("gpt-4o",
                "Bedöm svaret:" + answer.getAnswer()
                        + "om svaret är rätt börja svaret med Rätt annars börja svaret med Fel . Ge sen en kort beskrivning om hur du hade kommit fram till svaret max 30 ord",
                1,
                teacher);
        chatRequest.addMessage(new Message("assistant", question.getQuestion()));
        ChatRespons respons = restTemplate.postForObject(openAiApiUrl, chatRequest, ChatRespons.class);

        return respons;
    }

}
