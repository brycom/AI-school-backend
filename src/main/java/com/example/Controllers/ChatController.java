package com.example.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.Teacher;
import com.example.Models.Topic;
import com.example.Models.Dtos.QuestionDto;
import com.example.Models.OpenAiModels.ChatRespons;
import com.example.Services.OpenAiService;

@RestController
@RequestMapping("/chat")
@CrossOrigin("*")
public class ChatController {

    @Autowired
    private OpenAiService openAiService;

    @PostMapping("/question")
    public String postQuestion(@RequestBody QuestionDto question) {

        ChatRespons respons = openAiService.sendQuestion(question.getTopic(), question.getTeacher());

        return respons.getChoices().get(0).getMessage().getContent();
    }

    /*     @PostMapping("/answer")
    public String postAnswer(@RequestBody String prompt) {
    
        ChatRespons respons = openAiService.sendChatRespons(prompt);
    
        return respons.getChoices().get(0).getMessage().getContent();
    } */

}
