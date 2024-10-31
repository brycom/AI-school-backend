package com.example.Models.Dtos;

public class UserDto {

    private String username;
    private String email;
    private String name;
    private String subscription;

    public UserDto(String username, String email, String name, String subscription) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.subscription = subscription;
    }

    public UserDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

}
