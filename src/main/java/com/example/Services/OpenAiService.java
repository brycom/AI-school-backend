package com.example.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Models.Answer;
import com.example.Models.Question;
import com.example.Models.Teacher;
import com.example.Models.Topic;
import com.example.Models.OpenAiModels.ChatRequest;
import com.example.Models.OpenAiModels.ChatRespons;
import com.example.Models.OpenAiModels.Message;

@Service
public class OpenAiService {

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    private final RestTemplate restTemplate;

    public OpenAiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ChatRespons sendQuestion(Topic topic, Teacher teacher) {
        ChatRequest chatRequest = new ChatRequest("gpt-4o",
                "Generera en " + topic.getTopic() + " fråga om " + topic.getDescription() + "med en svårighetsgrad på:"
                        + topic.getLevel() + "av 10",
                1,
                teacher);
        ChatRespons respons = restTemplate.postForObject(openAiApiUrl, chatRequest, ChatRespons.class);

        return respons;
    }

    public ChatRespons sendAnswer(Answer answer, Question question, Teacher teacher) {
        ChatRequest chatRequest = new ChatRequest("gpt-4o",
                "Bedöm svaret:" + answer.getAnswer()
                        + " svara först om det är rätt eller fel. Ge sen en kort beskrivning om hur du hade kommit fram till svaret max 30 ord",
                1,
                teacher);
        chatRequest.addMessage(new Message("assistant", question.getQuestion()));
        ChatRespons respons = restTemplate.postForObject(openAiApiUrl, chatRequest, ChatRespons.class);

        return respons;
    }

}
