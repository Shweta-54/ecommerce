package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Cart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_cart);

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
        } else if (menuItemId == R.id.bottom_nav_Profile) {
            intent = new Intent(getApplicationContext(), Profile.class);
        } else if (menuItemId == R.id.bottom_nav_cart) {
            // Stay on the current activity
            return;
        }

        if (intent != null) {
            startActivity(intent);
            ActivityUtils.applyTransition(Cart.this); // Apply transition after starting the activity
            finish();
        }
    }
}