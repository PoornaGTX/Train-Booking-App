package com.example.trainbookingapp.network;

import com.example.trainbookingapp.service.ProfileService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileApiClient {

    private static final String BASE_URL = "https://6f48-2402-4000-21c2-2777-58ae-935b-f2f2-dbe8.ngrok-free.app/";
    private ProfileService profileService;

    // Constructor for initializing the ProfileApiClient
    public ProfileApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the ProfileService instance using Retrofit
        profileService = retrofit.create(ProfileService.class);
    }

    // method to retrieve the ProfileService instance
    public ProfileService getProfileService() {
        return profileService;
    }
}
