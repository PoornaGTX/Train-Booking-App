package com.example.trainbookingapp;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.trainbookingapp.model.Reservation;
import com.example.trainbookingapp.model.ReservationResponse;
import com.example.trainbookingapp.network.ReservationApiClient;
import com.example.trainbookingapp.utility.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private TextView noDataTextView;
    private ProgressBar loadingProgressBar;
    private Spinner startingPointSpinner;
    private Spinner destinationSpinner;

    // variable to store fetched reservations
    private List<Reservation> fetchedReservations = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        noDataTextView = view.findViewById(R.id.noDataTextView);

        // Initialize the spinners
        startingPointSpinner = view.findViewById(R.id.startingPointSpinner);
        destinationSpinner = view.findViewById(R.id.destinationSpinner);

        // Create an ArrayAdapter and set it to the spinners
        ArrayAdapter<CharSequence> startingPointAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.starting_points,
                android.R.layout.simple_spinner_item
        );
        startingPointAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startingPointSpinner.setAdapter(startingPointAdapter);

        ArrayAdapter<CharSequence> destinationAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.destinations,
                android.R.layout.simple_spinner_item
        );
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(destinationAdapter);

        // Set default values for spinners based on the data
        int startingPointDefaultPosition = startingPointAdapter.getPosition("Select Starting Point");
        if (startingPointDefaultPosition >= 0) {
            startingPointSpinner.setSelection(startingPointDefaultPosition);
        }

        int destinationDefaultPosition = destinationAdapter.getPosition("Select Destination");
        if (destinationDefaultPosition >= 0) {
            destinationSpinner.setSelection(destinationDefaultPosition);
        }

        // Initialize the loadingProgressBar
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        // Create an empty list of reservations
        List<Reservation> reservations = new ArrayList<>();

        // Create a adapter and pass the list of reservations
        adapter = new ReservationAdapter(reservations, this);

        // Set the adapter for the RecyclerView
        recyclerView.setAdapter(adapter);

        // Set a listener for the "Sort" button
        Button sortButton = view.findViewById(R.id.sortButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected starting point and destination
                String selectedStartingPoint = startingPointSpinner.getSelectedItem().toString();
                String selectedDestination = destinationSpinner.getSelectedItem().toString();

                // Filter and sort the reservations based on the selected criteria
                List<Reservation> sortedReservations = filterAndSortReservations(selectedStartingPoint, selectedDestination);

                // Update the adapter with the filtered and sorted reservations
                adapter.setReservations(sortedReservations);

                // Notify the adapter of the data change
                adapter.notifyDataSetChanged();
            }
        });

        Button clearButton = view.findViewById(R.id.clearButton);

        // handler for Clear button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the sorted data
                adapter.clearSortedData();

                // Clear the spinner selections and set back to default values
                startingPointSpinner.setSelection(0);
                destinationSpinner.setSelection(0);

                // Notify the adapter of the data change
                adapter.notifyDataSetChanged();
                fetchReservations();
            }
        });

        // check the network connection
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            fetchReservations();
        } else {
            noDataTextView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void fetchReservations() {
        loadingProgressBar.setVisibility(View.VISIBLE);

        ReservationApiClient reservationApiClient = new ReservationApiClient();
        reservationApiClient.getReservations(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response) {
                if (response.isSuccessful()) {
                    fetchedReservations = response.body().getSchedule();

                    // check the adapter
                    if (adapter != null) {
                        // Update the reservations list in the adapter
                        adapter.setReservations(fetchedReservations);

                        // Notify the adapter of the data change
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    noDataTextView.setVisibility(View.VISIBLE);
                }
                loadingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t) {
                Log.e("API Error", "API request failed", t);
                noDataTextView.setVisibility(View.VISIBLE);
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }

    // Method to filter and sort reservations based on selectedStartingPoint and selectedDestination
    private List<Reservation> filterAndSortReservations(String selectedStartingPoint, String selectedDestination) {
        // Iterate through fetchedReservations list and include only the ones that match the selected criteria
        boolean matchingFound = false;

        List<Reservation> sortedReservations = new ArrayList<>();
        for (Reservation reservation : fetchedReservations) {
            if (reservation.getStartingPoint().equals(selectedStartingPoint)
                    && reservation.getDestination().equals(selectedDestination)) {
                // Add the reservation to the sorted list
                sortedReservations.add(reservation);
                matchingFound = true;
            }
        }
        if (!matchingFound) {
            // if No matching reservations,display no data modal
            showNoDataModal();
        }
        return sortedReservations;
    }

    private void showNoDataModal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View modalView = LayoutInflater.from(requireContext()).inflate(R.layout.no_data_modal, null);

        // Set up the dialog
        builder.setView(modalView);
        AlertDialog dialog = builder.create();

        // handler for the Dismiss button
        Button dismissButton = modalView.findViewById(R.id.dismissButton);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fetchReservations();
            }
        });
        dialog.show();
    }
}