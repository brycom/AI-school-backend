package com.example.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.Models.MembershipTear;
import com.example.Models.User;
import com.example.Models.Dtos.PaymentRequestDto;
import com.example.Repositorys.MembershipTearRepository;
import com.example.Repositorys.UserRepository;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripePaymentService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private MembershipTearRepository tearsRepository;

    @Value("${stripe.api.key}")
    private String apiKey;

    @Value("${stripe.webhook.key}")
    private String webhookKey;

    public Map<String, String> CreateCheckoutSession(User user, PaymentRequestDto request) {
        Stripe.apiKey = apiKey;

        Map<String, String> responseData = new HashMap<>();

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomer(user.getCustumerNr())
                    .setSuccessUrl("https://whale-app-s5qc7.ondigitalocean.app/")
                    .setCancelUrl("https://whale-app-s5qc7.ondigitalocean.app/")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPrice(request.getPaymentId())
                            .build())
                    .build();
            Session session = Session.create(params);
            //response.redirect(session.getUrl(), 303);
            responseData.put("url", session.getUrl());

            return responseData;

        } catch (Exception e) {
            System.out.println("Failed to create checkout session: " + e.getMessage());
            e.printStackTrace();
            //return "Failed to create checkout session";
            return null;
        }

    }

    public String CreateCustomer(User user) {
        Stripe.apiKey = apiKey;
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .build();

        try {
            Customer customer = Customer.create(params);
            user.setSubscription(customer.getId());
            userRepository.save(user);
            return customer.getId();

        } catch (Exception e) {
            System.out.println("Failed to create customer: " + e.getMessage());
            return "Failed to create customer";
        }

    }

    public String CheckPayment(String paylode) {
        System.out.println("Checking payment");

        Stripe.apiKey = apiKey;
        // System.out.println("Hook key: " + webhookKey);
        System.out.println(
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!_____________________________________________________!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        Event event = null;

        try {
            event = ApiResource.GSON.fromJson(paylode, Event.class);

            if ("checkout.session.completed".equals(event.getType())) {
                @SuppressWarnings("deprecation")
                Session session = (Session) event.getData().getObject();
                String customerEmail = session.getCustomerDetails().getEmail();
                String paymentStatus = session.getPaymentStatus();
                int produktPrice = session.getAmountTotal().intValue() / 100;

                System.out.println("Customer Email: " + customerEmail);
                System.out.println("Payment Status: " + paymentStatus);
                System.out.println("Produkt pris: " + produktPrice);
                Optional<User> user = userRepository.findByEmail(customerEmail);
                if (user.isPresent() && paymentStatus.equals("paid")) {
                    MembershipTear tear = tearsRepository.findByPrice(produktPrice);
                    user.get().setSubscription(tear.getPriceId());
                    userRepository.save(user.get());
                } else {
                    System.out.println("Payment failed or user not found");
                    return "Payment failed";
                }

            } else {
                System.out.println("Unhandled event type: " + event.getType());
                System.out.println("Event:" + event.toString());
            }
        } catch (Exception e) {
            System.out.println("Failed to parse event: " + e.getMessage());
        }

        return "Payment Successful";

    }

}
