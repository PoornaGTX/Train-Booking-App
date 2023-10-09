package com.example.trainbookingapp.service;

import com.example.trainbookingapp.model.LoginRequestBody;
import com.example.trainbookingapp.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    //login route
    @POST("api/v1/auth/login")
    Call<LoginResponse> login(@Body LoginRequestBody request);
}
