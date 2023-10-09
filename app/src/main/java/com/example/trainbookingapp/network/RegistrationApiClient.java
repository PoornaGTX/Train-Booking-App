package com.example.trainbookingapp.network;

import com.example.trainbookingapp.model.RegistrationRequestBody;
import com.example.trainbookingapp.model.RegistrationResponse;
import com.example.trainbookingapp.service.RegistrationService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationApiClient {

    private final RegistrationService registrationService;
    private static final String BASE_URL = "https://6f48-2402-4000-21c2-2777-58ae-935b-f2f2-dbe8.ngrok-free.app/";

    public RegistrationApiClient() {

        // Create a logging interceptor for HTTP requests and responses
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);

        // Initialize Retrofit and create the RegistrationService instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientBuilder.build()) // Set the OkHttpClient with the interceptor
                .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON parsing
                .build();

        // Create an instance of RegistrationService interface
        registrationService = retrofit.create(RegistrationService.class);
    }

    public RegistrationService getRegistrationService() {
        // provide access to the RegistrationService instance
        return registrationService;
    }

    // method to initiates a registration API request
    public void register(RegistrationRequestBody requestBody, Callback<RegistrationResponse> callback) {
        // Create a Retrofit call for registration using RequestBody
        Call<RegistrationResponse> call = registrationService.register(requestBody);

        // Enqueue the request for asynchronous execution, passing the provided callback
        call.enqueue(callback);
    }
}
