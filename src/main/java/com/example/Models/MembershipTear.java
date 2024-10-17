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

    public MembershipTear(UUID id, String tear) {
        this.id = id;
        this.tear = tear;
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

}
