package com.example.trainbookingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.net.ParseException;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainbookingapp.model.Reservation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.example.trainbookingapp.R;


public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservations;
    private FragmentActivity activity;
    private Fragment fragment;
    private List<String> availableDates;
    private List<String> availableTimes;
    private List<String> selectedAvailableDates = new ArrayList<>();
    private List<String> selectedAvailableTimes = new ArrayList<>();
    private List<Reservation> fetchedReservations = new ArrayList<>();

    // Constructor to initialize the data
    public ReservationAdapter(List<Reservation> reservations, Fragment fragment) {
        this.reservations = reservations != null ? reservations : new ArrayList<>();
        this.fragment = fragment;
        this.availableDates = new ArrayList<>();
        this.availableTimes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item view layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        // get the current adapter position
        int adapterPosition = holder.getAdapterPosition();

        if (adapterPosition != RecyclerView.NO_POSITION) {
            Reservation reservation = reservations.get(adapterPosition);

            // Bind data to TextViews and set click listener for the Book button
            holder.destinationTextView.setText("Destination: " + reservation.getDestination());
            holder.startingPointTextView.setText("From: " + reservation.getStartingPoint());
            holder.dateTextView.setText("Date: " + reservation.getDate());
            holder.timeTextView.setText("Departure: " + reservation.getTime());
            holder.arrivalTimeTextView.setText("Arrival: " + reservation.getTimeTwo());
            holder.ticketPrice.setText("Ticket Price: Rs."+ reservation.getticketPrice());

            holder.bookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the selected reservation using adapterPosition
                    Reservation selectedReservation = reservations.get(adapterPosition);

                    // Calculate the difference in days between the booking date and the current date
                    Calendar currentDate = Calendar.getInstance();
                    currentDate.set(Calendar.HOUR_OF_DAY, 0);
                    currentDate.set(Calendar.MINUTE, 0);
                    currentDate.set(Calendar.SECOND, 0);
                    currentDate.set(Calendar.MILLISECOND, 0);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    // Store the selected available dates and times
                    selectedAvailableDates.clear();
                    selectedAvailableTimes.clear();

                    List<Map<String, String>> availableDatesList = selectedReservation.getAvailableDatesList();
                    for (Map<String, String> dateMap : availableDatesList) {
                        for (Map.Entry<String, String> entry : dateMap.entrySet()) {
                            String dateValue = entry.getValue();
                            selectedAvailableDates.add(dateValue);
                        }
                    }

                    List<Map<String, String>> availableTimeList = selectedReservation.getAvailableTimesList();
                    for (Map<String, String> timeMap : availableTimeList) {
                        for (Map.Entry<String, String> entry : timeMap.entrySet()) {
                            String timeValue = entry.getValue();
                            selectedAvailableTimes.add(timeValue);
                        }
                    }

                    try {
                        Date bookingDate = sdf.parse(reservation.getDate());
                        Calendar bookingDateCalendar = Calendar.getInstance();
                        bookingDateCalendar.setTime(bookingDate);

                        // Set the time components of the booking date to midnight
                        bookingDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                        bookingDateCalendar.set(Calendar.MINUTE, 0);
                        bookingDateCalendar.set(Calendar.SECOND, 0);
                        bookingDateCalendar.set(Calendar.MILLISECOND, 0);

                        long timeDifferenceInMillis = bookingDateCalendar.getTimeInMillis() - currentDate.getTimeInMillis();
                        long daysDifference = TimeUnit.MILLISECONDS.toDays(timeDifferenceInMillis);

                        if (daysDifference >= 30) {
                            // Check if the fragment reference is not null
                            if (fragment != null) {
                                // Create and show the booking confirmation dialog with reservation details
                                BookingConfirmationDialogFragment dialogFragment = new BookingConfirmationDialogFragment(selectedAvailableDates, selectedAvailableTimes);
                                dialogFragment.setReservationDetails(
                                        selectedReservation.getDestination(),
                                        selectedReservation.getStartingPoint(),
                                        selectedReservation.getDate(),
                                        selectedReservation.getTime(),
                                        selectedReservation.getTimeTwo(),
                                        selectedReservation.getID()
                                );
                                dialogFragment.show(fragment.requireActivity().getSupportFragmentManager(), "BookingConfirmationDialog");
                            }
                        }
                    else {
                        // Display a message to the user that the booking cannot be edited
                        showBookStatusDialog(false, "Train bookings can only be made at least 30 days in advance.");
                    }
                    } catch (ParseException | java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void showBookStatusDialog(boolean isSuccess, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setTitle(isSuccess ? "Success" : "Error");
        builder.setMessage(message);

        // handler for positive button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    // ViewHolder class to hold references to item view elements
    public class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView destinationTextView;
        TextView startingPointTextView;
        TextView timeTextView;

        TextView arrivalTimeTextView;

        TextView ticketPrice;

        TextView dateTextView;
        Button bookButton;

        public ReservationViewHolder(View itemView) {
            super(itemView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            startingPointTextView = itemView.findViewById(R.id.startingPointTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            arrivalTimeTextView = itemView.findViewById(R.id.arrivalTextView);
            ticketPrice = itemView.findViewById(R.id.ticketTextView);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }

    // Method to set new reservations data
    public void setReservations(List<Reservation> newReservations) {
        reservations.clear();
        reservations.addAll(newReservations);
        notifyDataSetChanged();
    }

    // method to get the reservations list
    public List<Reservation> getReservations() {
        return reservations;
    }

    // Method to clear the sorted data and restore original data
    public void clearSortedData() {
        reservations.clear();
        reservations.addAll(fetchedReservations);
        notifyDataSetChanged();
    }
}
