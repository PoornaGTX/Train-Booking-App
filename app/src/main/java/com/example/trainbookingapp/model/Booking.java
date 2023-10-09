package com.example.trainbookingapp.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Booking implements Serializable  {

    private String destination;
    private String startingPoint;
    private String date;
    private String time;
    private String userEmail;
    private List<Map<String, String>> availableDates;
    private List<Map<String, String>> availableTimes;

    private String _id;

    // Constructor
    public Booking(String destination, String startingPoint, String time, String date,String _id) {
        this.destination = destination;
        this.startingPoint = startingPoint;
        this.time = time;
        this.date = date;
        this._id = _id;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = _id;
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
