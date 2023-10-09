package com.example.trainbookingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.trainbookingapp.model.ProfileUpdateRequest;
import com.example.trainbookingapp.network.ProfileApiClient;
import com.example.trainbookingapp.service.ProfileService;
import com.example.trainbookingapp.utility.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private SharedPreferencesManager sharedPreferencesManager;
    private EditText fullNameTextView, nicTextView, emailTextView;
    private Button editProfileButton, logoutButton;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        // Initialize views
        fullNameTextView = view.findViewById(R.id.fullNameEditText);
        nicTextView = view.findViewById(R.id.nicEditText);
        emailTextView = view.findViewById(R.id.emailEditText);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Initialize the views and click listeners
        Button logoutButton = view.findViewById(R.id.logoutButton);

        String fullName = sharedPreferencesManager.getUsername();
        String nic = sharedPreferencesManager.getNIC();
        String email = sharedPreferencesManager.getEmail();

        fullNameTextView.setText(fullName);
        nicTextView.setText(nic);
        emailTextView.setText(email);

        // handler for edit button
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show edit profile dialog
                showEditProfileDialog(fullName,email);
            }
        });

        // handler for logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void showEditProfileDialog(String currentFullName ,String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null);
        builder.setView(dialogView);

        EditText fullNameEditText = dialogView.findViewById(R.id.fullNameEditText);
        fullNameEditText.setText(currentFullName);

        builder.setTitle("Edit Profile");

        //handler for update button
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newFullName = fullNameEditText.getText().toString();
                // Call API to update user's full name
                updateFullName(newFullName,email);
            }
        });

        //handler for cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateFullName(String newFullName,String email) {
        // Update the UI with the new full name if successful
        fullNameTextView.setText(newFullName);

        // save the updated full name in SharedPreferences
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        sharedPreferencesManager.savefullName(newFullName);

        // Create an instance of ProfileApiClient
        ProfileApiClient profileApiClient = new ProfileApiClient();
        ProfileService profileService = profileApiClient.getProfileService();

        // Get the userEmail
        String useremail = email;

        // Get the updated full name from the user input
        String updatedFullName = newFullName;

        // Create the update request
        ProfileUpdateRequest request = new ProfileUpdateRequest(useremail,updatedFullName);

        // Make the API call to update the full name
        Call<Void> call = profileService.updateFullName(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showProfileUpdateStatusDialog(true, "Profile updated successfully.");
                } else {
                    showProfileUpdateStatusDialog(false, "Failed to update profile.");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showProfileUpdateStatusDialog(false, "Error: "+t);
            }
        });
    }

    private void showProfileUpdateStatusDialog(boolean isSuccess, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(isSuccess ? "Success" : "Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logout() {
        // Clear user data from SharedPreferences
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        sharedPreferencesManager.clearUserData();

        // Navigate to the login activity
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}