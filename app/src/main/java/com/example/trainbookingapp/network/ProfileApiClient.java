package com.example.trainbookingapp.network;

import com.example.trainbookingapp.model.ProfileUpdateRequest;
import com.example.trainbookingapp.service.ProfileService;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileApiClient {

    private static final String BASE_URL = "https://a63f-2402-4000-20c2-707-bc9d-1d76-fa31-916e.ngrok-free.app/";
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

    //method to deactivate the account
    public Call<Void> deactivateAccount(String userNIC, ProfileUpdateRequest request) {
        return profileService.deactivateAccount(userNIC, request);
    }
}
