package com.example.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OpenAiService openAiService;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
    }

    @PostMapping("/question")
    public void handleQuestion(@RequestBody QuestionDto question) {
        System.out.println("vald lärares namn:  " + question.getTeacher().getName());
        openAiService.sendQuestionStream(question.getTopic(), question.getTeacher());
        /*         String messageContent = respons.getChoices().get(0).getMessage().getContent();
        System.out.println("Respons från OpenAI: " + messageContent);
        
        Map<String, String> message = new HashMap<>();
        message.put("content", messageContent);
        messagingTemplate.convertAndSend("/topic", message); */
    }

    @PostMapping("/answer")
    public Message postAnswer(@RequestBody AnswerDto answer) {

        ChatRespons respons = openAiService.sendAnswer(answer.getAnswer(), answer.getQuestion(), answer.getTeacher());
        List<Choice> choices = respons.getChoices();

        return choices.get(0).getMessage();
    }

}
