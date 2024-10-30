package com.example.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.User;
import com.example.Models.Dtos.PaymentRequestDto;
import com.example.Repositorys.UserRepository;
import com.example.Services.StripePaymentService;
import com.example.Services.UserService;

@RestController
@RequestMapping("/stripe")
@CrossOrigin("*")
public class StripePaymentController {

    private UserService userService;
    private StripePaymentService paymentService;
    private UserRepository userRepository;

    public StripePaymentController(UserService userService, StripePaymentService paymentService,
            UserRepository userRepository) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }

    @PostMapping("/payment")
    public Map<String, String> payWithStripe(@RequestBody PaymentRequestDto request) {
        System.out.println(request.getPaymentId());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);

        if (request.getPaymentId().equals("")) {
            System.out.println("Här ska det stå ingenting:" + request.getPaymentId());
            user.setSubscription("free");
            userRepository.save(user);
            Map<String, String> responseData = new HashMap<>();
            responseData.put("url", "http://localhost:5173/topic-selector");
            return responseData;

        }
        System.out.println("Det här borde du inte se????");
        if (user == null) {
            return null;
        }
        if (user.getSubscription() == null) {
            user.setSubscription(paymentService.CreateCustomer(user));
        }

        return paymentService.CreateCheckoutSession(user, request);
    }

    @PostMapping("/newCustomer")
    public String createCustomer() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);

        if (user == null) {
            return "User not found";
        }
        if (user.getSubscription() == null) {
            user.setSubscription(paymentService.CreateCustomer(user));
        }

        return "User created successfully";
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload) {
        try {

            String responseMessage = paymentService.CheckPayment(payload);

            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            System.out.println("Error processing webhook: " + e.getMessage());
            return ResponseEntity.status(500).body("Error processing webhook");
        }
    }

}
