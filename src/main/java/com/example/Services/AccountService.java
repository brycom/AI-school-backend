package com.example.Services;

import org.springframework.stereotype.Service;

import com.example.Models.User;
import com.example.Repositorys.UserRepository;

@Service
public class AccountService {

    private UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createAccount(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already in use: " + user.getUsername());
        }

        return userRepository.save(user);

    }

}
