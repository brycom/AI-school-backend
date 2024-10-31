package com.example.Controllers;

import java.util.List;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.Dtos.AnswerDto;
import com.example.Models.Dtos.QuestionDto;
import com.example.Models.OpenAiModels.ChatRespons;
import com.example.Models.OpenAiModels.Message;
import com.example.Models.OpenAiModels.ChatRespons.Choice;
import com.example.Services.OpenAiService;

@RestController
@RequestMapping("/chat")
@CrossOrigin("*")
public class ChatController {

    private OpenAiService openAiService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate, OpenAiService openAiService) {
        this.messagingTemplate = messagingTemplate;
        this.openAiService = openAiService;
    }

    @PostMapping("/question")
    public void handleQuestion(@RequestBody QuestionDto question) {
        ChatRespons respons = openAiService.sendQuestion(question.getTopic(), question.getTeacher());
        messagingTemplate.convertAndSend("/topic", respons.getChoices().get(0).getMessage().getContent());
    }

    @PostMapping("/answer")
    public Message postAnswer(@RequestBody AnswerDto answer) {

        ChatRespons respons = openAiService.sendAnswer(answer.getAnswer(), answer.getQuestion(), answer.getTeacher());
        List<Choice> choices = respons.getChoices();

        return choices.get(0).getMessage();
    }

}
