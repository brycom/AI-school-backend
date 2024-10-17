package com.example.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.Topic;
import com.example.Services.TopicService;

@RestController
@RequestMapping("/admin/topic")
@CrossOrigin("*")
public class TopicController {

    public TopicService topicService;

    public TopicController(TopicService toppicService) {
        this.topicService = toppicService;
    }

    @PostMapping("/newTopic")
    public String newTopic(@RequestBody Topic topic) {
        topicService.addNewTopic(topic);
        return "new topic added successfully";
    }

}
