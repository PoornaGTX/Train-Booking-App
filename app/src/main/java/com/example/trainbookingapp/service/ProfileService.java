package com.example.trainbookingapp.service;

import com.example.trainbookingapp.model.ProfileUpdateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;

public interface ProfileService {

    //update profile route
    @PATCH("api/v1/auth/updateUser")
    Call<Void> updateFullName(@Body ProfileUpdateRequest request);
}
