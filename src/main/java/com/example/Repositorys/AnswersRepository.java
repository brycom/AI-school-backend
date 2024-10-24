package com.example.Repositorys;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Models.Answer;

@Repository
public interface AnswersRepository extends CrudRepository<Answer, Integer> {

}