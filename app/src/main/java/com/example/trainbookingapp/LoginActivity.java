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

import com.example.trainbookingapp.model.LoginRequestBody;
import com.example.trainbookingapp.model.LoginResponse;
import com.example.trainbookingapp.network.LoginApiClient;
import com.example.trainbookingapp.utility.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        //login button handler
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        String savedUsername = sharedPreferencesManager.getUsername();

        String savedEmail = sharedPreferencesManager.getEmail();

        if (savedUsername != null && savedEmail != null) {
            // The user is already logged in, navigate to the main activity
            navigateToMainActivity();
        }
    }

    private void login() {

        // Get the email and password entered by the user
        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            // alert to empty fields
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Error");
            builder.setMessage("Please enter both email and password");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        // Create LoginRequestBody object with email and password
        LoginRequestBody requestBody = new LoginRequestBody(email, password);

        // Create an instance of LoginApiClient to make the login API request
        LoginApiClient loginApiClient = new LoginApiClient();
        loginApiClient.login(requestBody, new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                String LoginnSuccess = "Login success";
                String LoginnError = "Login Error";

                if (response.isSuccessful()) {
                    // Parse the login response
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        String fullName = loginResponse.getfullName();
                        String nic = loginResponse.getNIC();

                        // Save user data to SharedPreferences using SharedPreferencesManager
                        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(LoginActivity.this);
                        sharedPreferencesManager.saveUserData(fullName,nic,email);

                        // Show a success dialog for login
                        showLoginStatusDialog(true, LoginnSuccess);

                        // Redirect to the main activity
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                    }
                                },
                                2000
                        );
                    }
                } else {
                    showLoginStatusDialog(false, LoginnError);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Handler for network or API request failure
                Toast.makeText(LoginActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // handler to navigate the registration
    public void goToRegistration(View view) {
        // Start the RegistrationActivity when the link is clicked
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //alert
    private void showLoginStatusDialog(boolean isSuccess, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isSuccess ? "Success" : "Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                // Check, if the login was successful before navigating to MainActivity
                if (isSuccess) {
                    navigateToMainActivity();
                }
            }
        });

        if (!isSuccess) {
            // If login is unsuccessful
            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}