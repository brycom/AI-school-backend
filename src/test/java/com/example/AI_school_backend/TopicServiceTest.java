package com.example.AI_school_backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.Models.Topic;
import com.example.Repositorys.TopicsRepository;
import com.example.Services.TopicService;

public class TopicServiceTest {

    @Mock
    private TopicsRepository topicsRepository;

    @InjectMocks
    private TopicService topicService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewTopic() {

        Topic topic = new Topic();
        topic.setTitle("Math");
        topic.setDescription("Mathematics");
        topic.setLevel(5);
        topic.setTopic("Bråk");

        when(topicsRepository.save(topic)).thenReturn(topic);

        Topic result = topicService.addNewTopic(topic);

        assertEquals(topic, result, "Expected the saved topic to be returned");
        verify(topicsRepository).save(topic);
    }

    @Test
    public void testGetAllTopics() {
        int size = 0;
        List<Topic> topics = Arrays.asList(new Topic(), new Topic());
        when(topicsRepository.findAll()).thenReturn(topics);

        Iterable<Topic> result = topicService.getAllTopics();
        for (Topic topic : result) {
            size++;
        }

        assertEquals(topics.size(), size, "Expected all topics to be returned");
    }

}
