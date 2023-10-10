package com.example.trainbookingapp.network;

import com.example.trainbookingapp.model.BookingRequest;
import com.example.trainbookingapp.model.BookingResponse;
import com.example.trainbookingapp.model.BookingUpdateOperation;
import com.example.trainbookingapp.model.BookingUpdateRequest;
import com.example.trainbookingapp.service.BookingService;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingApiClient {

    private final BookingService bookingService;

    private static final String BASE_URL = "https://f3cc-2a09-bac1-4360-00-279-9d.ngrok-free.app/";

    public BookingApiClient() {
        // Initialize the Retrofit client and create the BookingService instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bookingService = retrofit.create(BookingService.class);
    }

    // book train method
    public void bookTrain(BookingRequest bookingRequest, Callback<BookingResponse> callback) {
        Call<BookingResponse> call = bookingService.bookTrain(bookingRequest);
        call.enqueue(callback);
    }

    // get all the user bookings
    public void getUserBooking(String userEmail, Callback<BookingResponse> callback) {
        // API request to get user booking data
        Call<BookingResponse> call = bookingService.getUserBooking(userEmail);

        // Enqueue the request for asynchronous execution
        call.enqueue(callback);
    }

    public void updateBooking(String bookingId, List<BookingUpdateOperation> updateOperations, Callback<BookingResponse> callback) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(updateOperations);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        Call<BookingResponse> call = bookingService.updateBooking(bookingId, requestBody);
        call.enqueue(callback);
    }


    // Create a method to delete a booking by ID
    public void deleteBooking(String bookingId, Callback<Void> callback) {
        // DELETE request
        Call<Void> call = bookingService.deleteBooking(bookingId);
        call.enqueue(callback); // Execute the request asynchronously
    }
}
