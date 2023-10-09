package com.example.trainbookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment home = new HomeFragment();
    ProfileFragment profile = new ProfileFragment();
    UserBookingsFragment resv = new UserBookingsFragment();

    private NavController navController;

    private void updateToolbarTitle(int itemId) {
        String title;
        if (itemId == R.id.navigation_home) {
            title = "Home";
        } else if (itemId == R.id.navigation_reservation) {
            title = "My Reservations";
        } else if (itemId == R.id.navigation_profile) {
            title = "Profile";
        } else {
            title = "Home";
        }
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the Toolbar
        setSupportActionBar(toolbar);

        // start fragmentTransaction
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // replace the container by Home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, home).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, home).commit();
                } else if (itemId == R.id.navigation_reservation) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, resv).commit();
                } else if (itemId == R.id.navigation_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, profile).commit();
                }

                // Update the Toolbar title by the user selected fragment
                updateToolbarTitle(itemId);

                return true;
            }
        });
    }
}