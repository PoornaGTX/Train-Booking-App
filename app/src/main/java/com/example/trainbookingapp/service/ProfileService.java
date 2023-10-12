package com.example.trainbookingapp.service;

import com.example.trainbookingapp.model.BookingResponse;
import com.example.trainbookingapp.model.ProfileUpdateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProfileService {

    //update profile route
    @PUT("api/Users/{nic}")
    Call<Void> updateFullName(@Path("nic") String nic, @Body ProfileUpdateRequest request);

    @PUT("api/Users/isActive/")
    Call<Void> deactivateAccount(@Query("nic") String userNIC, @Body ProfileUpdateRequest request);
}
