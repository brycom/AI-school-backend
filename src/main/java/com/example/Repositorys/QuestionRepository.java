package com.example.Repositorys;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Models.Question;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {

}
