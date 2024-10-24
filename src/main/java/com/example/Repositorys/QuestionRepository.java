package com.example.Repositorys;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Models.Question;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {

    Question findById(UUID id);

    Question findByQuestion(String question);

    @Query("SELECT q FROM Question q WHERE q.question = ?1 AND q.userId = ?2")
    Question findByQuestionAndUser(String message, UUID userId);

    void findByUserId(UUID userId);

    /* @Query("SELECT * FROM User u WHERE u.userId =?1") */
    List<Question> findAllByUserId(UUID userId);

    @Query("SELECT q FROM  Question q WHERE q.userId = ?1 AND q.topicId = ?2")
    List<Question> findAllByUserIdAndTopicId(UUID userId, UUID topicId);

}
