package com.example.trainbookingapp.model;

public class ProfileUpdateRequest {
    private String fullName;
    private String email;

    private String nic;

    private boolean isActivate;

    public  ProfileUpdateRequest(){}

    public ProfileUpdateRequest(String email,String fullName) {
        this.fullName = fullName;
        this.email=email;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public void setNIC(String nic) {
        this.nic = nic;
    }

    public String getNIC() {
        return nic;
    }



}
