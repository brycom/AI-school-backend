package com.example.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.MembershipTear;
import com.example.Models.User;
import com.example.Models.Dtos.UserDto;
import com.example.Repositorys.MembershipTearRepository;
import com.example.Repositorys.UserRepository;

@RestController
@RequestMapping("/account")
public class AccountController {

    UserRepository userRepository;
    MembershipTearRepository membershipTearRepository;

    public AccountController(UserRepository userRepository, MembershipTearRepository membershipTearRepository) {
        this.userRepository = userRepository;
        this.membershipTearRepository = membershipTearRepository;
    }

    @GetMapping("/profile")
    public UserDto getProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            System.out.println("Användarens tear: " + user.get().getSubscription());
            UserDto utd = new UserDto();
            utd.setUsername(user.get().getUsername());
            utd.setEmail(user.get().getEmail());
            utd.setName(user.get().getName());
            if (user.get().getSubscription().isEmpty() || user.get().getSubscription() == null) {
                utd.setSubscription("Användaren är ej abonnenterad.");
            } else {

                MembershipTear tear = membershipTearRepository.findByPriceId(user.get().getSubscription());
                System.out.println(tear.getPriceId());
                utd.setSubscription(tear.getTear());
            }
            return utd;
        }
        return null;
    }

}
