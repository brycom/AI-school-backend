package com.example.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Models.Teacher;
import com.example.Models.Topic;
import com.example.Models.OpenAiModels.ChatRequest;
import com.example.Models.OpenAiModels.ChatRespons;

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

}
