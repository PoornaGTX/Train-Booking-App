package com.example.trainbookingapp.service;

import com.example.trainbookingapp.model.BookingRequest;
import com.example.trainbookingapp.model.BookingResponse;
import com.example.trainbookingapp.model.BookingUpdateRequest;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookingService {

    //book train route
    @POST("api/Bookings")
    Call<BookingResponse> bookTrain(@Body BookingRequest bookingRequest);

    //get user bookings route
    @GET("api/Bookings/filter")
    Call<BookingResponse> getUserBooking(@Query("userEmail") String userEmail);

    //update booking route
    @PATCH("api/Bookings/{id}")
    Call<BookingResponse> updateBooking(
            @Path("id") String id,
            @Body RequestBody requestBody
    );

    //cancel booking route
    @DELETE("/api/Bookings/{id}")
    Call<Void> deleteBooking(@Path("id") String id);
}
