package com.example.trainbookingapp.model;

public class ProfileUpdateRequest {
    private String fullName;
    private String email;

    public ProfileUpdateRequest(String email,String fullName) {
        this.fullName = fullName;
        this.email=email;
    }

    public String getFullName() {
        return fullName;
    }
}
