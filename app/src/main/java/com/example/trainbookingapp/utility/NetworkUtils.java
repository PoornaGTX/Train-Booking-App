package com.example.trainbookingapp.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkUtils {

    //method to check if the device is currently connected to a network.
    public static boolean isNetworkConnected(Context context) {
        //Get a reference to the ConnectivityManager system service.
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Check if the ConnectivityManager reference is not null.
        if (cm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Get the currently active network.
                Network activeNetwork = cm.getActiveNetwork();
                if (activeNetwork != null) {
                    NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
                    //Check if the network capabilities indicate a Wi-Fi or cellular connection.
                    return capabilities != null &&
                            (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
                }
            } else {
                //Get information about the active network.
                NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }

        return false;
    }
}
