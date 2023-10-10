package com.example.trainbookingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReservationResponse {

    @SerializedName("shedule")
    private List<Reservation> shedule;

    public List<Reservation> getSchedule() {
        return shedule;
    }
}
