package com.example.Models.Dtos;

import java.util.UUID;

public class PaymentRequestDto {

    private String paymentId;
    private UUID tearId;

    public PaymentRequestDto(String paymentId, UUID tearId) {
        this.paymentId = paymentId;
        this.tearId = tearId;
    }

    public PaymentRequestDto() {
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public UUID getTearId() {
        return tearId;
    }

    public void setTearId(UUID tearId) {
        this.tearId = tearId;
    }

}
