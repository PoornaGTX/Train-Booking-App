package com.example.trainbookingapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainbookingapp.model.AvailableDates;
import com.example.trainbookingapp.model.AvailableTimes;
import com.example.trainbookingapp.model.BookingRequest;
import com.example.trainbookingapp.model.BookingResponse;
import com.example.trainbookingapp.network.BookingApiClient;
import com.example.trainbookingapp.utility.SharedPreferencesManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingConfirmationDialogFragment extends DialogFragment {

    private String destination;
    private String startingPoint;
    private String date;
    private String time;
    private String time2;
    private String sheduleID;
    private String name;
    private List<String> selectedAvailableDates;
    private List<String> selectedAvailableTimes;
    private Handler handler;
    private String ticketPrice;

    // Constructor to initialize the selected available dates and times
    public BookingConfirmationDialogFragment() {
    }

    public BookingConfirmationDialogFragment(List<String> selectedAvailableDates, List<String> selectedAvailableTimes) {
        this.selectedAvailableDates = selectedAvailableDates;
        this.selectedAvailableTimes = selectedAvailableTimes;

    }

    // Method to set reservation details
    public void setReservationDetails(String destination, String startingPoint, String date, String time, String time2, String sheduleID, String name,String ticketPrice) {
        this.destination = destination;
        this.startingPoint = startingPoint;
        this.date = date;
        this.time = time;
        this.time2 = time2;
        this.sheduleID = sheduleID;
        this.name = name;
        this.ticketPrice = ticketPrice;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        handler = new Handler(Looper.getMainLooper());

        if (!isAdded()) {
            // Fragment is not attached to a context, return a default dialog
            return super.onCreateDialog(savedInstanceState);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());


        // Inflate the dialog layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_booking_confirmation, null);

        // Initialize views and set text
        TextView destinationTextView = view.findViewById(R.id.destinationTextView);
        TextView startingTextView = view.findViewById(R.id.startingTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        TextView timeTextView = view.findViewById(R.id.timeTextView);
        TextView trainTextView = view.findViewById(R.id.trainTextView);
        TextView ticketTextView = view.findViewById(R.id.ticketpriceTextView);


        startingTextView.setText("Starting Station: " + startingPoint);
        destinationTextView.setText("Destination Station: " + destination);
        dateTextView.setText("Date: " + date);
        timeTextView.setText("Time: " + time);
        trainTextView.setText("Train: " + name);
        ticketTextView.setText("Ticket price: "+ ticketPrice+"0");

        // Initialize buttons and set click listeners
        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        //confirmation handler
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to send the booking request
                bookTrain();
            }
        });

        //cancel button handler
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Set the view for the dialog
        builder.setView(view);

        // Create and return the dialog
        return builder.create();
    }

    //schedule notification
    private void scheduleNotificationOneDayBefore() {
        // Parse the selected booking date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date bookingDate = sdf.parse(date);

            // Calculate the notification date, one day before the booking date
            Calendar notificationCal = Calendar.getInstance();
            notificationCal.setTime(bookingDate);
            notificationCal.add(Calendar.DAY_OF_MONTH, -1);

            // Set up the AlarmManager to schedule the notification
            AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(requireContext(), NotificationReceiver.class);
            notificationIntent.putExtra("message", "Train reminder: " + destination + " train at " + time);

            // unique request code to identify notification
            int requestCode = 0;

            PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Set the notification to be triggered one day before the booking date
            long notificationTime = notificationCal.getTimeInMillis();
            alarmManager.set(AlarmManager.RTC, notificationTime, pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    // Success alert
    private void showSuccessDialog() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
                alertDialogBuilder.setTitle("Success");
                alertDialogBuilder.setMessage("Booking has been updated successfully.");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    // Method to show an error dialog
    private void showErrorDialog(final String errorMsg) {
        if (isAdded()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Error");
            builder.setMessage(errorMsg);
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Log.e("FragmentError", "Fragment is not added to an activity.");
        }
    }


    // Method to send the booking request to the backend API
    private void bookTrain() {
        // Create a BookingRequest with reservation details and user email
        SharedPreferencesManager sharedPreferencesManager;
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        String nic = sharedPreferencesManager.getNIC();

        BookingRequest request = new BookingRequest();
        request.setDestination(destination);
        request.setStartingPoint(startingPoint);
        request.setDate(date);
        request.setTime(time);
        request.setTimeTwo(time2);
        request.setID(sheduleID);
        request.setNIC(nic);
        request.setTicketPrice(ticketPrice);
        request.setName(name);


        AvailableDates availableDates = new AvailableDates();
        AvailableTimes availableTimes = new AvailableTimes();

        // Populate the instances with selected available dates and times
        availableDates.setDate1(selectedAvailableDates.get(0));
        availableDates.setDate2(selectedAvailableDates.get(1));
        availableDates.setDate3(selectedAvailableDates.get(2));

        availableTimes.setTime1(selectedAvailableTimes.get(0));
        availableTimes.setTime2(selectedAvailableTimes.get(1));
        availableTimes.setTime3(selectedAvailableTimes.get(2));

        // Set the instances in the BookingRequest
        request.setAvailableDates(availableDates);
        request.setAvailableTimes(availableTimes);

        // get user email from sharedPreference
        String userEmail = sharedPreferencesManager.getEmail();

        // Set the user email in the request
        request.setUserEmail(userEmail);

        // Store the context
        Context context = requireContext();

        // Create an instance of BookingApiClient
        BookingApiClient bookingApiClient = new BookingApiClient();

        // Make the API request to book the train
        bookingApiClient.bookTrain(request, new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                if (context != null) {
                    if (response.isSuccessful()) {
                        showSuccessDialog();
                        scheduleNotificationOneDayBefore();
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                //handle the error body
                                JSONObject errorJson = new JSONObject(response.errorBody().string());
                                String errorMsg = errorJson.getString("msg");
                                showErrorDialog(errorMsg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("API Error", "Failed to update booking: " + response.code());
                        }
                    }
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                if (context != null) {
                    Toast.makeText(context, "Booking failed. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
    }}