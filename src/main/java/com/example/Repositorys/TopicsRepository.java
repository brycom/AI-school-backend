package com.example.Repositorys;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Models.Topic;

@Repository
public interface TopicsRepository extends CrudRepository<Topic, Integer> {

    // List<Topic> findAll();

}
