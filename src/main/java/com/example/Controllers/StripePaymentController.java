package com.example.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);

        if (request.getPaymentId().equals("free")) {
            user.setSubscription("free");
            userRepository.save(user);
            Map<String, String> responseData = new HashMap<>();
            responseData.put("url", "https://whale-app-s5qc7.ondigitalocean.app/");
            return responseData;

        }
        if (user == null) {
            return null;
        }
        if (user.getCustumerNr() == null || user.getCustumerNr().isEmpty()) {
            user.setCustumerNr(paymentService.CreateCustomer(user));
        }
        //behöver både kundnummer och pris id 

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

    @PostMapping(value = "/webhook", consumes = "application/json")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload) {
        System.out.println("hallå i stugan!");
        try {

            String responseMessage = paymentService.CheckPayment(payload);

            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            System.out.println("Error processing webhook: " + e.getMessage());
            return ResponseEntity.status(500).body("Error processing webhook");
        }
    }

}
