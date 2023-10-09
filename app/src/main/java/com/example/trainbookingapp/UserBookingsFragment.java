package com.example.trainbookingapp;

import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.trainbookingapp.db.BookingNewDB;
import com.example.trainbookingapp.model.Booking;
import com.example.trainbookingapp.model.BookingResponse;
import com.example.trainbookingapp.model.BookingSQL;
import com.example.trainbookingapp.network.BookingApiClient;
import com.example.trainbookingapp.utility.NetworkUtils;
import com.example.trainbookingapp.utility.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBookingsFragment extends Fragment implements BookingAdapter.OnBookingDeletedListener,BookingEditDialogFragment.OnBookingUpdatedListener  {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private ProgressBar loadingProgressBar;
    private SharedPreferencesManager sharedPreferencesManager;
    private TextView noDataTextView;
    private BookingNewDB bookingNewDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_bookings, container, false);

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        //local db instance
        bookingNewDB= new BookingNewDB(requireContext());

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewBooking);

        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        noDataTextView = view.findViewById(R.id.noDataTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        bookingAdapter = new BookingAdapter(new ArrayList<>(), requireContext(), this);

        recyclerView.setAdapter(bookingAdapter);

        bookingAdapter.setOnItemClickListener(new BookingAdapter.OnItemClickListener() {
            //editButton handler
            @Override
            public void onEditClick(Booking booking) {
                // Show the edit dialog fragment
                BookingEditDialogFragment dialogFragment = BookingEditDialogFragment.newInstance(booking);

                // Set the reference to the UserBookingsFragment
                dialogFragment.setUserBookingFragment(UserBookingsFragment.this);
                dialogFragment.show(getChildFragmentManager(), "edit_dialog");
            }
            @Override
            public void onCloseClick(Booking booking) {
            }
        });
        fetchBookingData();
        return view;
    }


    private void fetchBookingData() {
        loadingProgressBar.setVisibility(View.GONE);

        if (NetworkUtils.isNetworkConnected(requireContext())) {
            // If the network is available, fetch data from the remote database
            loadingProgressBar.setVisibility(View.VISIBLE);
            fetchBookingDataAndUpdateUI();
        } else {
            // If the network is not available, fetch data from the local SQLite database
            fetchBookingsFromSQLiteAndUpdateUI();
        }
    }

    private void fetchBookingDataAndUpdateUI() {
        loadingProgressBar.setVisibility(View.VISIBLE);

        BookingApiClient bookingApiClient = new BookingApiClient();
        String userEmail = sharedPreferencesManager.getEmail();

        Callback<BookingResponse> callback = new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                loadingProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    BookingResponse bookingResponse = response.body();
                    List<Booking> bookings = bookingResponse.getBookings();

                    recyclerView.setVisibility(View.VISIBLE);
                    noDataTextView.setVisibility(View.GONE);

                    if (bookings != null && !bookings.isEmpty()) {
                        // Iterate through the bookings and parse available dates and times
                        for (Booking booking : bookings) {
                            List<String> availableDates = new ArrayList<>();
                            List<String> availableTimes = new ArrayList<>();

                            for (Map<String, String> dateMap : booking.getAvailableDates()) {
                                for (String date : dateMap.values()) {
                                    availableDates.add(date);
                                }
                            }

                            for (Map<String, String> timeMap : booking.getAvailableTimes()) {
                                for (String time : timeMap.values()) {
                                    availableTimes.add(time);
                                }
                            }

                            // Update the booking object with parsed available dates and times
                            Map<String, String> availableDatesMap = new HashMap<>();
                            for (int i = 0; i < availableDates.size(); i++) {
                                availableDatesMap.put("date" + (i + 1), availableDates.get(i));
                            }

                            Map<String, String> availableTimesMap = new HashMap<>();
                            for (int i = 0; i < availableTimes.size(); i++) {
                                availableTimesMap.put("time" + (i + 1), availableTimes.get(i));
                            }

                            booking.setAvailableDates(Collections.singletonList(availableDatesMap));
                            booking.setAvailableTimes(Collections.singletonList(availableTimesMap));
                        }

                        for (Booking booking : bookings) {
                            // Insert the current booking item into the database
                            insertBookingToDatabase(booking);
                        }

                        bookingAdapter.setBookings(bookings);
                        bookingAdapter.notifyDataSetChanged();
                    }else {
                        Log.d("onBookingDeletedListener", "nodata");
                        recyclerView.setVisibility(View.GONE);
                        noDataTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("error", "error");
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                loadingProgressBar.setVisibility(View.GONE);
                Log.d("API error", "error: " + t);
            }
        };
        bookingApiClient.getUserBooking(userEmail, callback);
    }

    private void fetchBookingsFromSQLiteAndUpdateUI() {
        List<BookingSQL> bookingSQLList = bookingNewDB.getAllBooking();

        // Convert BookingSQL objects to Booking objects if needed
        List<Booking> bookings = new ArrayList<>();
        for (BookingSQL bookingSQL : bookingSQLList) {
            Booking booking = new Booking(
                    bookingSQL.getDestination(),
                    bookingSQL.getStartingPoint(),
                    bookingSQL.getDate(),
                    bookingSQL.getTime(),
                    bookingSQL.get_id()
            );
            booking.getTime();
            bookings.add(booking);
        }

        if (!bookings.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            noDataTextView.setVisibility(View.GONE);
            bookingAdapter.setBookings(bookings);
            bookingAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
            noDataTextView.setVisibility(View.VISIBLE);
        }
    }

    // Method to save a booking to SQLite
    private void insertBookingToDatabase(Booking booking) {

        boolean isBookingExists = bookingNewDB.isBookingExists(booking.getId());

        // Create a BookingSQL object from the Booking data
        if (!isBookingExists) {
            // Create a BookingSQL object from the Booking data
            BookingSQL bookingSQL = new BookingSQL(
                    booking.getDestination(),
                    booking.getStartingPoint(),
                    booking.getDate(),
                    booking.getTime(),
                    sharedPreferencesManager.getEmail(),
                    booking.getId()
            );
            // Insert the BookingSQL object into the SQLite database
            bookingNewDB.insertBooking(bookingSQL);
        }
    }

    @Override
    public void onBookingDeleted() {
        Log.d("onBookingDeletedListener", "onBookingDeletedListener");
        fetchBookingDataAndUpdateUI();
    }

    @Override
    public void onBookingUpdated() {
        fetchBookingDataAndUpdateUI();
    }
}