package com.example.trainbookingapp.service;

import com.example.trainbookingapp.model.ReservationResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReservationService {

    //get schedules route
    @GET("api/Schedules")
    Call<ReservationResponse> getReservations();
}
