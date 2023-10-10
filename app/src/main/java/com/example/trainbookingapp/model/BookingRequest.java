package com.example.trainbookingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class BookingRequest {

    private String scheduleID;

    private String nic;
    private String destination;
    private String startingPoint;
    private String date;
    private String arrivalTimeToEndStation;

    private String departureTimeFromStartStation;

    private String userEmail;

    @SerializedName("availableDates")
    private AvailableDates availableDates;

    @SerializedName("availableTimes")
    private AvailableTimes availableTimes;

    public AvailableDates getAvailableDates() {
        return availableDates;
    }

    public String getID() {
        return scheduleID;
    }

    public void setID(String scheduleID) {
        this.scheduleID = scheduleID;
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
        return arrivalTimeToEndStation;
    }

    public void setTime(String arrivalTimeToEndStation) {
        this.arrivalTimeToEndStation = arrivalTimeToEndStation;
    }

    public String getTimeTwo() {
        return departureTimeFromStartStation;
    }

    public void setTimeTwo(String departureTimeFromStartStation) {
        this.departureTimeFromStartStation = departureTimeFromStartStation;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setAvailableDates(AvailableDates availableDates) {
        this.availableDates = availableDates;
    }

    public AvailableTimes getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(AvailableTimes availableTimes) {
        this.availableTimes = availableTimes;
    }


}
