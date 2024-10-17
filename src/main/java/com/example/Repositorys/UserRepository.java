package com.example.Repositorys;

import java.util.List;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByUsername(String username);

}
