package com.example.trainbookingapp.service;

import com.example.trainbookingapp.model.RegistrationRequestBody;
import com.example.trainbookingapp.model.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistrationService {

    //register user route
    @POST("api/Users/register")
    Call<RegistrationResponse> register(@Body RegistrationRequestBody request);
}
