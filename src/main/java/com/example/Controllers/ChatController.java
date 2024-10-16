package com.example.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.OpenAiModels.ChatRespons;
import com.example.Services.OpenAiService;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private OpenAiService openAiService;

    @PostMapping("/postChat")
    public String postChat(@RequestBody String prompt) {

        ChatRespons respons = openAiService.sendChatRespons(prompt);

        return respons.getChoices().get(0).getMessage().getContent();
    }

}
