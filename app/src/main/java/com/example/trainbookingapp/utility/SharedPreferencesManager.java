package com.example.trainbookingapp.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_FULLNAME = "fullName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NIC = "nic";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    // Constructor initializes SharedPreferences and SharedPreferences.Editor
    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Method to save user data to SharedPreferences
    public void saveUserData(String fullName,String nic, String email) {
        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_NIC, nic);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Method to save full name to SharedPreferences
    public void savefullName(String fullName) {
        editor.putString(KEY_FULLNAME, fullName);
        editor.apply();
    }


    // Method to retrieve full name from SharedPreferences
    public String getUsername() {
        return sharedPreferences.getString(KEY_FULLNAME, null);
    }

    // Method to retrieve NIC from SharedPreferences
    public String getNIC() {
        return sharedPreferences.getString(KEY_NIC, null);
    }

    // Method to retrieve email from SharedPreferences
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }


    // Method to clear user data from SharedPreferences
    public void clearUserData() {
        editor.remove(KEY_FULLNAME);
        editor.remove(KEY_NIC);
        editor.remove(KEY_EMAIL);
        editor.apply();
    }
}
