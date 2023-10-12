package com.example.trainbookingapp.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("fullName") // annotation to maps the JSON field username to the Java field
    private String fullName;
    private  String nic;

    public String getfullName() {
        return fullName;
    }

    public void setfullName(String fullName) {
        this.fullName = fullName;
    }

    public  String getNIC(){return nic;}

    public void setNIC(String nic) {
        this.nic = nic;
    }
}
