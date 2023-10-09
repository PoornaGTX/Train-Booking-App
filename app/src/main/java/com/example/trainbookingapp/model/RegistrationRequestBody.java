package com.example.trainbookingapp.model;

public class RegistrationRequestBody {

    private String fullName;
    private String nic;
    private String email;
    private String password;

    public RegistrationRequestBody(String fullName,String nic, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.nic=nic;
    }
}
