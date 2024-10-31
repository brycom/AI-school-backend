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

    public Map<String, String> CreateCheckoutSession(User user, PaymentRequestDto request) {
        Stripe.apiKey = apiKey;
        System.out.println("apiKey:" + apiKey);

        Map<String, String> responseData = new HashMap<>();

        try {
            System.out.println("Kommer jag hit da?  " + request.getPaymentId());
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomer(user.getSubscription())
                    .setSuccessUrl("http://localhost:5173/account")
                    .setCancelUrl("http://localhost:5173/account")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPrice(request.getPaymentId())
                            .build())
                    .build();
            System.out.println("Kommer jag hit da?  " + request.getPaymentId());
            Session session = Session.create(params);
            //response.redirect(session.getUrl(), 303);
            System.out.println(session.getUrl());
            responseData.put("url", session.getUrl());
            System.out.println("Nu har jag slut på ider");

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
        System.out.println(user.getName());
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .build();

        try {
            Customer customer = Customer.create(params);
            System.out.println("hallå i stugan");
            System.out.println(customer);
            user.setSubscription(customer.getId());
            userRepository.save(user);
            return customer.getId();

        } catch (Exception e) {
            System.out.println("Failed to create customer: " + e.getMessage());
            return "Failed to create customer";
        }

    }

    public String CheckPayment(String paylode) {

        Stripe.apiKey = apiKey;

        Event event = null;

        try {
            event = ApiResource.GSON.fromJson(paylode, Event.class);

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getData().getObject();
                String customerEmail = session.getCustomerEmail();
                String paymentStatus = session.getPaymentStatus();
                int produktPrice = session.getAmountTotal().intValue();

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
            }
        } catch (Exception e) {
            System.out.println("Failed to parse event: " + e.getMessage());
        }

        return "Payment Successful";

    }

}
