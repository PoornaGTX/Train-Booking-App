package com.example.trainbookingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class BookingRequest {


    private String _id;

    private String nic;
    private String destination;
    private String startingPoint;
    private String date;
    private String time;
    private String userEmail;

    @SerializedName("availableDates")
    private List<Map<String, String>> availableDates;

    @SerializedName("availableTimes")
    private List<Map<String, String>> availableTimes;

    public String getID() {
        return _id;
    }

    public void setID(String _id) {
        this._id = _id;
    }

    public String getNIC() {
        return nic;
    }

    public void setNIC(String nic) {
        this.nic = nic;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<Map<String, String>> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<Map<String, String>> availableDates) {
        this.availableDates = availableDates;
    }

    public List<Map<String, String>> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<Map<String, String>> availableTimes) {
        this.availableTimes = availableTimes;
    }
}
