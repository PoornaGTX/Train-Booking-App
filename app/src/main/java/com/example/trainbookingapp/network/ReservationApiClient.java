package com.example.trainbookingapp.network;

import android.util.Log;

import com.example.trainbookingapp.model.ReservationResponse;
import com.example.trainbookingapp.service.ReservationService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReservationApiClient {
    private final ReservationService reservationService;
    private static final String BASE_URL = "https://f3cc-2a09-bac1-4360-00-279-9d.ngrok-free.app/";

    public ReservationApiClient() {
        // Create a logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Create an OkHttpClient with the logging interceptor
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);

        // Initialize Retrofit and create the ReservationService instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientBuilder.build()) // Set the OkHttpClient with the interceptor
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        reservationService = retrofit.create(ReservationService.class);
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public void getReservations(Callback<ReservationResponse> callback) {
        // Make the API request to get reservations
        Call<ReservationResponse> call = reservationService.getReservations();

        // Enqueue the request for asynchronous execution
        call.enqueue(callback);
    }
}
