package com.example.Services;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.Models.User;
import com.example.Models.Dtos.PaymentRequestDto;
import com.example.Repositorys.UserRepository;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripePaymentService {
    @Autowired
    UserRepository userRepository;

    @Value("${stripe.api.key}")
    private String apiKey;

    public String CreateCheckoutSession(User user, PaymentRequestDto request) {
        Stripe.apiKey = apiKey;

        try {

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomer(user.getSubscription())
                    .setSuccessUrl("http://localhost:5173/success")
                    .setCancelUrl("http://localhost:5173/cancel")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPrice("price_1QFBrjA7lbBlr7pVbx6BK9VU")
                            .build())
                    .build();
            Session session = Session.create(params);
            // response.redirect(session.getUrl(), 303);

            return session.getUrl();

        } catch (Exception e) {
            System.out.println("Failed to create checkout session: " + e.getMessage());
            return "Failed to create checkout session";
        }

    }

    public String CreateCustomer(User user) {
        Stripe.apiKey = apiKey;
        System.out.println(user.getName());
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .build();

        try {
            Customer customer = Customer.create(params);
            System.out.println(customer);
            user.setSubscription(customer.getId());
            userRepository.save(user);
            return customer.getId();

        } catch (Exception e) {
            System.out.println("Failed to create customer: " + e.getMessage());
            return "Failed to create customer";
        }

    }

    //Stripe.apiKey = "sk_test_51QFB0TA7lbBlr7pVQPOsTmLVGwKctPRUeVNDm8C54p4NAutGUYvVu3Tn5EOJ49WIcuRxA1w6y6TPvauJOvGbfjnL00KV4Kmpkr";

    /*     staticFiles.externalLocation(
        Paths.get("public").toAbsolutePath().toString());
    
    post("/create-checkout-session", (request, response) -> {
        String YOUR_DOMAIN = "http://localhost:4242";
        SessionCreateParams params =
          SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(YOUR_DOMAIN + "?success=true")
            .setCancelUrl(YOUR_DOMAIN + "?canceled=true")
            .addLineItem(
              SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                // Provide the exact Price ID (for example, pr_1234) of the product you want to sell
                .setPrice("price_1QFBrjA7lbBlr7pVbx6BK9VU")
                .build())
            .build();
      Session session = Session.create(params);
    
      response.redirect(session.getUrl(), 303);
      return "";
    });
     */
}
