package com.example.trainbookingapp.service;

import com.example.trainbookingapp.model.BookingRequest;
import com.example.trainbookingapp.model.BookingResponse;
import com.example.trainbookingapp.model.BookingUpdateRequest;

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
    @POST("api/v1/shedule/book")
    Call<BookingResponse> bookTrain(@Body BookingRequest bookingRequest);

    //get user bookings route
    @GET("api/v1/shedule/book")
    Call<BookingResponse> getUserBooking(@Query("userEmail") String userEmail);

    //update booking route
    @PATCH("api/v1/shedule/bookupdate/{id}")
    Call<BookingResponse> updateBooking(
            @Path("id") String _id,
            @Body BookingUpdateRequest updateRequest
    );

    //cancel booking
    @DELETE("/api/v1/shedule/bookupdate/{id}")
    Call<Void> deleteBooking(@Path("id") String id);
}
