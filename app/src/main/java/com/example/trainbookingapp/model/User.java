package com.example.trainbookingapp.model;

public class User {
    public String nic;
    public String fullName;
    public String password;
    public String email;

    public void setfullName(String fullName) {
        this.fullName = fullName;
    }

    public String getfullName() {
        return fullName;
    }

    public void setNIC(String nic) {
        this.nic = nic;
    }

    public String getNIC() {
        return nic;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
