package com.example.Controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.Question;
import com.example.Services.QuestionService;
import com.example.Services.UserService;

@RestController
@RequestMapping("/questions")
@CrossOrigin("*")
public class QuestionController {

    private QuestionService questionService;
    private UserService userService;

    public QuestionController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    @GetMapping("last-ten/{topic}")
    public List<Question> getLastTenQuestions(@PathVariable String topic) {
        UUID topicId = UUID.fromString(topic);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = userService.getUserIdFromUsername(username);
        System.out.println("Här är den aktiva användaren: " + username);
        List<Question> q = questionService.getLastTenQuestions(userId, topicId);
        System.out.println(q.get(0).getQuestion());
        return q;
    }

}
