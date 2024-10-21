package com.example.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @PostMapping("/question")
    public Message postQuestion(@RequestBody QuestionDto question) {

        ChatRespons respons = openAiService.sendQuestion(question.getTopic(), question.getTeacher());

        return respons.getChoices().get(0).getMessage();
    }

    @PostMapping("/answer")
    public Message postAnswer(@RequestBody AnswerDto answer) {

        ChatRespons respons = openAiService.sendAnswer(answer.getAnswer(), answer.getQuestion(), answer.getTeacher());
        List<Choice> choices = respons.getChoices();

        return choices.get(0).getMessage();
    }

}
