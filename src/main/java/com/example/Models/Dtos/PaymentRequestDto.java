package com.example.Models.Dtos;

import com.example.Models.Subscription;

public class PaymentRequestDto {

    private String paymentId;
    //private Subscription subscription;

    public PaymentRequestDto(String paymentId /* Subscription subscription */) {
        this.paymentId = paymentId;
        //this.subscription = subscription;
    }

    public PaymentRequestDto() {
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    /*     public Subscription getSubscription() {
        return subscription;
    }
    
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
     */
}
