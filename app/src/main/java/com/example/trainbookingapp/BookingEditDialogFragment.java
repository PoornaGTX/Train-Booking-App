package com.example.trainbookingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainbookingapp.model.Booking;
import com.example.trainbookingapp.model.BookingResponse;
import com.example.trainbookingapp.model.BookingUpdateOperation;
import com.example.trainbookingapp.model.BookingUpdateRequest;
import com.example.trainbookingapp.network.BookingApiClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingEditDialogFragment extends DialogFragment {
    private Booking booking;
    private Button customSaveButton;
    private RadioGroup dateRadioGroup;
    private RadioGroup timeRadioGroup;
    private AlertDialog dialog;
    private TextView dateTextView;
    private TextView timeTextView;
    private UserBookingsFragment reservationFragment;

    private Handler handler;


    public BookingEditDialogFragment() {
        // empty public constructor
    }

    public static BookingEditDialogFragment newInstance(Booking booking) {
        BookingEditDialogFragment fragment = new BookingEditDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("booking", booking);
        fragment.setArguments(args);
        return fragment;
    }

    BookingUpdateRequest updateRequest = new BookingUpdateRequest();

    public void setUserBookingFragment(UserBookingsFragment fragment) {
        this.reservationFragment = fragment;
    }

    public interface OnBookingUpdatedListener {
        void onBookingUpdated();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        handler = new Handler(Looper.getMainLooper());

        if (!isAdded()) {
            // Fragment is not attached to a context, return a default dialog
            return super.onCreateDialog(savedInstanceState);
        }

        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_booking_edit, null);

        // Retrieve booking data from arguments
        if (getArguments() != null) {
            booking = (Booking) getArguments().getSerializable("booking");
        }

        // Define custom buttons and set click listeners
        customSaveButton = view.findViewById(R.id.customSaveButton);
        Button customCancelButton = view.findViewById(R.id.customCancelButton);

        // Disable the Save button initially
        customSaveButton.setEnabled(false);

        TextView startingPointTextView = view.findViewById(R.id.startingPointTextView);
        TextView destinationTextView = view.findViewById(R.id.destinationTextView);

        dateTextView = view.findViewById(R.id.dateTextView);
        timeTextView = view.findViewById(R.id.timeTextView);

        // Set text for date and time
        dateTextView.setText("Date: " + booking.getDate());
        timeTextView.setText("Time: " + booking.getTime());


        TextView dateTextView = view.findViewById(R.id.dateTextView);
        TextView timeTextView = view.findViewById(R.id.timeTextView);

        startingPointTextView.setText("Starting Point: " + booking.getStartingPoint());
        destinationTextView.setText("Destination: " + booking.getDestination());


        // Add click listeners for date and time selections
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateSelectionDialog();
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelectionDialog();
            }
        });

        // Populate available dates and times with radio buttons
        dateRadioGroup = view.findViewById(R.id.availableDatesRadioGroup);
        timeRadioGroup = view.findViewById(R.id.availableTimesRadioGroup);

        TextView availableDatesHeader = view.findViewById(R.id.availableDatesHeader);
        TextView availableTimesHeader = view.findViewById(R.id.availableTimesHeader);

        populateAvailableDates(dateRadioGroup, booking.getAvailableDates());
        populateAvailableTimes(timeRadioGroup, booking.getAvailableTimes());

        availableDatesHeader.setVisibility(View.VISIBLE);
        availableTimesHeader.setVisibility(View.VISIBLE);

        // Make the available dates section visible
        dateRadioGroup.setVisibility(View.VISIBLE);
        // Make the available times section visible
        timeRadioGroup.setVisibility(View.VISIBLE);

        // Set up listeners for date and time selections
        setupDateSelectionListener(dateTextView);
        setupTimeSelectionListener(timeTextView);

        customSaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handleCustomSaveButtonClick();
            }
        });

        customCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Set the view for the dialog
        builder.setView(view);

        // Return the customized dialog
        return builder.create();
    }

    // Method to show a success dialog
    private void showSuccessDialog() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Success");
                builder.setMessage("Booking has been updated successfully.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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

    //date selection handler
    private void setupDateSelectionListener(final TextView dateTextView) {
        dateRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = group.findViewById(checkedId);
                if (selectedRadioButton != null) {
                    // Get the text (date) from the selected radio button
                    String selectedDate = selectedRadioButton.getText().toString();

                    // Set text for date
                    dateTextView.setText("Date: " + selectedDate);
                    dateTextView.setTextColor(Color.RED);

                    // Update the updateRequest object
                    updateRequest.setDate(selectedDate);

                    // Enable the save button
                    enableSaveButtonIfBothDateAndTimeSelected();
                }
            }
        });
    }

    //time selection handler
    private void setupTimeSelectionListener(final TextView timeTextView) {
        timeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = group.findViewById(checkedId);
                if (selectedRadioButton != null) {
                    // Get the text (time) from the selected radio button
                    String selectedTime = selectedRadioButton.getText().toString();

                    // Set text for time
                    timeTextView.setText("Time: " + selectedTime);
                    timeTextView.setTextColor(Color.RED);

                    // Update the updateRequest object
                    updateRequest.setTime(selectedTime);

                    // Enable the save button
                    enableSaveButtonIfBothDateAndTimeSelected();
                }
            }
        });
    }

    private void enableSaveButtonIfBothDateAndTimeSelected() {
        boolean isDateSelected = dateRadioGroup.getCheckedRadioButtonId() != -1;
        boolean isTimeSelected = timeRadioGroup.getCheckedRadioButtonId() != -1;
        customSaveButton.setEnabled(isDateSelected || isTimeSelected);

        // Set the button text color based on its enabled state
        customSaveButton.setTextColor(getResources().getColor(
                isDateSelected || isTimeSelected ?  R.color.white: R.color.disabled_button_color));
    }


    private void handleCustomSaveButtonClick() {
        // Check if a radio button is selected before proceeding
        int selectedRadioButtonId = timeRadioGroup.getCheckedRadioButtonId();
        int selectedRadioButton_id = dateRadioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId != -1 || selectedRadioButton_id != -1 ) {
            // A radio button is selected, continue
            updateRequest.set_id(booking.getId());
            updateRequest.setDestination(booking.getDestination());
            updateRequest.setStartingPoint(booking.getStartingPoint());

            if (updateRequest.getDate() == null) {
                updateRequest.setDate(booking.getDate());
            }

            if (updateRequest.getTime() == null) {
                updateRequest.setTime(booking.getTimeTwo());
            }

            List<BookingUpdateOperation> updateOperations = new ArrayList<>();

            //patch request refactoring the data
            updateOperations.add(new BookingUpdateOperation("date", updateRequest.getDate()));
            updateOperations.add(new BookingUpdateOperation("departureTimeFromStartStation", updateRequest.getTime()));

            // Call the method to send the PATCH request with the updateRequest
            BookingApiClient bookingApiClient = new BookingApiClient();
            bookingApiClient.updateBooking(booking.getId(), updateOperations, new Callback<BookingResponse>() {
                @Override
                public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                    if (response.isSuccessful()) {
                        BookingResponse bookingResponse = response.body();
                        if (bookingResponse != null) {
                            //success aler
                            showSuccessDialog();
                            if (getParentFragment() instanceof OnBookingUpdatedListener) {
                                ((OnBookingUpdatedListener) getParentFragment()).onBookingUpdated();
                            }
                        }
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                JSONObject errorJson = new JSONObject(response.errorBody().string());
                                String errorMsg = errorJson.getString("msg");
                                //error alert
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

                @Override
                public void onFailure(Call<BookingResponse> call, Throwable t) {
                    Log.e("API Error", "Failed to update booking: " + t.getMessage());
                }
            });
        } else {
            // No radio button is selected, show an error message
            Toast.makeText(requireContext(), "Please select a time.", Toast.LENGTH_SHORT).show();
        }

    }



    private void handleCustomCancelButtonClick() {
        dismiss();
    }

    // Helper method to populate available dates with radio buttons
    private void populateAvailableDates(RadioGroup radioGroup, Map<String, String> availableDatesMap) {
        for (Map.Entry<String, String> entry : availableDatesMap.entrySet()) {
            String dateKey = entry.getKey();
            String dateValue = entry.getValue();

            RadioButton radioButton = new RadioButton(requireContext());
            radioButton.setText(dateValue);
            radioButton.setTag(dateValue); // Store the date value as a tag
            radioGroup.addView(radioButton);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedDate = (String) v.getTag();
                    // Set text for date
                    dateTextView.setText("Date: " + selectedDate);
                    dateTextView.setTextColor(Color.RED);

                    // Update the updateRequest object
                    updateRequest.setDate(selectedDate);

                    // Enable the save button
                    enableSaveButtonIfBothDateAndTimeSelected();
                }
            });
        }
    }

    private void populateAvailableTimes(RadioGroup radioGroup, Map<String, String> availableTimesMap) {
        for (Map.Entry<String, String> entry : availableTimesMap.entrySet()) {
            String timeKey = entry.getKey();
            String timeValue = entry.getValue();

            RadioButton radioButton = new RadioButton(requireContext());
            radioButton.setText(timeValue);
            radioButton.setTag(timeValue); // Store the time value as a tag
            radioGroup.addView(radioButton);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedTime = (String) v.getTag();
                    // Set text for time
                    timeTextView.setText("Time: " + selectedTime);
                    timeTextView.setTextColor(Color.RED);

                    // Update the updateRequest object
                    updateRequest.setTime(selectedTime);

                    // Enable the save button
                    enableSaveButtonIfBothDateAndTimeSelected();
                }
            });
        }
    }


    private void showDateSelectionDialog() {
    }

    private void showTimeSelectionDialog() {
    }
}