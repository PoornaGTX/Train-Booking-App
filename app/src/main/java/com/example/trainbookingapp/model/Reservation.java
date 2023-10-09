package com.example.trainbookingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Reservation {

    private String _id;
    private String destination;
    private String startingPoint;
    private String date;
    private String time;
    @SerializedName("availableDates")
    private List<Map<String, String>> availableDatesList;

    @SerializedName("availableTimes")
    private List<Map<String, String>> availableTimesList;

    public String getID() {
        return _id;
    }

    public void setID(String _id) {
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

    public List<Map<String, String>> getAvailableDatesList() {
        return availableDatesList;
    }

    public void setAvailableDatesList(List<Map<String, String>> availableDatesList) {
        this.availableDatesList = availableDatesList;
    }

    public List<Map<String, String>> getAvailableTimesList() {
        return availableTimesList;
    }

    public void setAvailableTimesList(List<Map<String, String>> availableTimesList) {
        this.availableTimesList = availableTimesList;
    }
}
