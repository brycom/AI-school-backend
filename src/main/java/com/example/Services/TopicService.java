package com.example.Services;

import org.springframework.stereotype.Service;

import com.example.Models.Topic;
import com.example.Repositorys.TopicsRepository;

@Service
public class TopicService {

    TopicsRepository topicsRepository;

    public TopicService(TopicsRepository topicsRepository) {
        this.topicsRepository = topicsRepository;
    }

    public Topic addNewTopic(Topic topic) {
        return topicsRepository.save(topic);
    }

    public Iterable<Topic> getAllTopics() {
        return topicsRepository.findAll();
    }

}
