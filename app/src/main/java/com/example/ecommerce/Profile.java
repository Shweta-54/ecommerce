package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_Profile);

        bottomNavigationView.setOnItemSelectedListener(menuItem ->{
            handleNavigation(menuItem.getItemId());
            return true;
        });
    }

    private void handleNavigation(int menuItemId) {
        Intent intent = null;
        if (menuItemId == R.id.bottom_nav_home) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        } else if (menuItemId == R.id.bottom_nav_categories) {
            intent = new Intent(getApplicationContext(), Categories.class);
        } else if (menuItemId == R.id.bottom_nav_cart) {
            intent = new Intent(getApplicationContext(), Cart.class);
        } else if (menuItemId == R.id.bottom_nav_Profile) {
            // Stay on the current shweta
            return;
        }
        if (intent != null) {
            startActivity(intent);
            ActivityUtils.applyTransition(this);
            finish();
        }
    }
}