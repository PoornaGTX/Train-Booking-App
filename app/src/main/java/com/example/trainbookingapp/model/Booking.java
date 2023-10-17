package com.example.trainbookingapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Booking implements Serializable  {

    private String destination;
    private String startingPoint;
    private String date;
    private String arrivalTimeToEndStation;
    private String departureTimeFromStartStation;
    private String ticketPrice;
    private String userEmail;
    @SerializedName("availableDates")
    private Map<String, String> availableDates;

    @SerializedName("availableTimes")
    private Map<String, String> availableTimes;
    private String id;
    private String scheduleID;

    // Constructor
    public Booking(String destination, String startingPoint, String date,String scheduleID,String arrivalTimeToEndStation) {
        this.destination = destination;
        this.startingPoint = startingPoint;
        this.arrivalTimeToEndStation = arrivalTimeToEndStation;
        this.date = date;
        this.scheduleID = scheduleID;

    }

    public String getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(String sheduleID) {
        this.scheduleID = scheduleID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getticketPrice() {
        return ticketPrice;
    }

    public void setticketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public Map<String, String> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(Map<String, String> availableDates) {
        this.availableDates = availableDates;
    }

    public Map<String, String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(Map<String, String> availableTimes) {
        this.availableTimes = availableTimes;
    }
}
