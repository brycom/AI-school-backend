package com.example.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.Topic;
import com.example.Services.JwtService;
import com.example.Services.TopicService;

@RestController
@RequestMapping("/topic")
@CrossOrigin("*")
public class TopicController {

    public TopicService topicService;

    public TopicController(TopicService toppicService, JwtService jwtService) {
        this.topicService = toppicService;
    }

    @PostMapping("admin/newTopic")
    public String newTopic(@RequestBody Topic topic) {
        topicService.addNewTopic(topic);
        return "new topic added successfully";
    }

    @GetMapping("/allTopics")
    public Iterable<Topic> allTopics() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("Användarnamn borde stämma: " + username);
            return topicService.getAllTopics();
        } catch (Exception e) {
            System.out.println("Ett fel uppstod: " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkToken() {
        return ResponseEntity.ok("Token is valid");

    }

}
