package com.example.Controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.User;
import com.example.Models.Dtos.PaymentRequestDto;
import com.example.Services.StripePaymentService;
import com.example.Services.UserService;

@RestController
@RequestMapping("/stripe")
@CrossOrigin("*")
public class StripePaymentController {

    private UserService userService;
    private StripePaymentService paymentService;

    public StripePaymentController(UserService userService, StripePaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public String payWithStripe(@RequestBody PaymentRequestDto request) {
        System.out.println(request.getPaymentId());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);

        if (user == null) {
            return "User not found";
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

}
