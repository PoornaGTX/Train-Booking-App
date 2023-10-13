package com.example.trainbookingapp.network;

import com.example.trainbookingapp.model.LoginRequestBody;
import com.example.trainbookingapp.model.LoginResponse;
import com.example.trainbookingapp.service.LoginService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginApiClient {

    private final LoginService loginService;
    private static final String BASE_URL = "http://thameera-001-site1.itempurl.com/";

    // Constructor to initialize the LoginApiClient
    public LoginApiClient() {

        // logging interceptor for HTTP requests and responses
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Create an OkHttpClient with the logging interceptor
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);

        // Initialize Retrofit and create the LoginService instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientBuilder.build()) // Set the OkHttpClient with the interceptor
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        loginService = retrofit.create(LoginService.class);
    }

    // Method to perform login API request
    public void login(LoginRequestBody requestBody, Callback<LoginResponse> callback) {
        // Create a Retrofit call for the login request using RequestBody
        Call<LoginResponse> call = loginService.login(requestBody);

        // Enqueue the request for asynchronous execution
        call.enqueue(callback);
    }
}
