package com.example.trainbookingapp.service;

import com.example.trainbookingapp.model.ProfileUpdateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProfileService {

    //update profile route
    @PATCH("api/v1/auth/updateUser")
    Call<Void> updateFullName(@Body ProfileUpdateRequest request);

    @PUT("api/v1/auth/updateUser/{nic}")
    Call<Void> deactivateAccount(@Path("nic") String userNIC, @Body ProfileUpdateRequest request);
}
