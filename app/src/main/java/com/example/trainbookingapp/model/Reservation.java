package com.example.trainbookingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Reservation {

    private String id;
    private String destination;
    private String startingPoint;
    private String date;
    private String arrivalTimeToEndStation;

    private String departureTimeFromStartStation;

    private String ticketPrice;


    @SerializedName("availableDates")
    private List<Map<String, String>> availableDates;

    @SerializedName("availableTimes")
    private List<Map<String, String>> availableTimes;

    public String getID() {
        return id;
    }

    public void setID(String id) {
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

    public List<Map<String, String>> getAvailableDatesList() {
        return availableDates;
    }

    public void setAvailableDatesList(List<Map<String, String>> availableDatesList) {
        this.availableDates = availableDates;
    }

    public List<Map<String, String>> getAvailableTimesList() {
        return availableTimes;
    }

    public void setAvailableTimesList(List<Map<String, String>> availableTimes) {
        this.availableTimes = availableTimes;
    }
}
