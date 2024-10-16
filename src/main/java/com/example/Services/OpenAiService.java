package com.example.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public ChatRespons sendChatRespons(String prompt) {
        ChatRequest chatRequest = new ChatRequest("gpt-4o", prompt, 1);
        ChatRespons respons = restTemplate.postForObject(openAiApiUrl, chatRequest, ChatRespons.class);

        return respons;
    }

}
