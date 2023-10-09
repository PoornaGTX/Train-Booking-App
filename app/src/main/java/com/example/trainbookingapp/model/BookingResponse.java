package com.example.trainbookingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingResponse {

    @SerializedName("bookings")
    private List<Booking> bookings;

    public List<Booking> getBookings() {
        return bookings;
    }
}
