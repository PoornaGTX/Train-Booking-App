package com.example.trainbookingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReservationResponse {

    @SerializedName("shedule")
    private List<Reservation> schedule;

    public List<Reservation> getSchedule() {
        return schedule;
    }
}
