package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Categories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_categories);

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
            // Stay on the current shweta
            return;
        } else if (menuItemId == R.id.bottom_nav_Profile) {
            intent = new Intent(getApplicationContext(), Profile.class);
        } else if (menuItemId == R.id.bottom_nav_cart) {
            intent = new Intent(getApplicationContext(), Cart.class);
        }

        if (intent != null) {
            startActivity(intent);
            ActivityUtils.applyTransition(Categories.this); // Apply transition after starting the activity
            finish();
        }
    }
}