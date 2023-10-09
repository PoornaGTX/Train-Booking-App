package com.example.trainbookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trainbookingapp.model.RegistrationRequestBody;
import com.example.trainbookingapp.model.RegistrationResponse;
import com.example.trainbookingapp.network.RegistrationApiClient;
import com.example.trainbookingapp.service.RegistrationService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText, emailEditText, passwordEditText, nicEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        fullNameEditText = findViewById(R.id.fullnameEditText);
        nicEditText = findViewById(R.id.nicEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button registerButton = findViewById(R.id.registerButton);

        //register button handler
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    //register function
    private void register() {
        String fullName = fullNameEditText.getText().toString();
        String nic = nicEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Create a RegistrationRequestBody object with username, email, and password
        RegistrationRequestBody requestBody = new RegistrationRequestBody(fullName, nic, email, password);

        RegistrationApiClient registrationApiClient = new RegistrationApiClient();

        // Get the service instance
        RegistrationService registrationService = registrationApiClient.getRegistrationService();

        Call<RegistrationResponse> call = registrationService.register(requestBody);

        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {

                // If the response is successful, parse the RegistrationResponse
                if (response.isSuccessful()) {
                    RegistrationResponse registrationResponse = response.body();

                    if (registrationResponse != null) {
                        String registrationMessage = registrationResponse.getMessage();

                        // Check if the registrationMessage is not null or empty before proceeding
                        if (registrationMessage != null && !registrationMessage.isEmpty()) {

                            // if Registration was successful show success dialog,
                            showRegistrationStatusDialog(true, registrationMessage);

                            // Redirect to the login activity
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            // Redirect to the LoginActivity
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    },
                                    3000
                            );
                        } else {
                            // Handler for if registrationMessage is empty or null
                            showRegistrationStatusDialog(false, "Registration message is empty.");
                        }
                    }
                } else {
                    if (response.errorBody() != null) {
                        try {
                            // get the error message from the error response
                            JSONObject errorJson = new JSONObject(response.errorBody().string());
                            String errorMsg = errorJson.getString("msg");
                            showRegistrationStatusDialog(false, errorMsg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Log an error message if the response code indicates an error
                        showRegistrationStatusDialog(false, "Error:"+ response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                showRegistrationStatusDialog(false, "Error:"+ t);
            }
        });
    }

    //alert
    private void showRegistrationStatusDialog(boolean isSuccess, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
}