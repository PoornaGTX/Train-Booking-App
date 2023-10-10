package com.example.trainbookingapp.network;

import com.example.trainbookingapp.model.ProfileUpdateRequest;
import com.example.trainbookingapp.service.ProfileService;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileApiClient {

    private static final String BASE_URL = "https://f3cc-2a09-bac1-4360-00-279-9d.ngrok-free.app/";
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

    public Call<Void> deactivateAccount(String userNIC, ProfileUpdateRequest request) {
        return profileService.deactivateAccount(userNIC, request);
    }
}
