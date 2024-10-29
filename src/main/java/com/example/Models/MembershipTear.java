package com.example.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
public class MembershipTear {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String tear;
    private String priceId;
    private String descripiton;
    private int price;

    public MembershipTear(UUID id, String tear, String priceId, String descripiton, int price) {
        this.id = id;
        this.tear = tear;
        this.priceId = priceId;
        this.price = price;
        this.descripiton = descripiton;
    }

    public MembershipTear() {
    }

    public UUID getId() {
        return id;
    }

    public String getTear() {
        return tear;
    }

    public void setTear(String tear) {
        this.tear = tear;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getDescripiton() {
        return descripiton;
    }

    public void setDescripiton(String descripiton) {
        this.descripiton = descripiton;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
