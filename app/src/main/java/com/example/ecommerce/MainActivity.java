package com.example.ecommerce;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//images click event.
        ImageView shopiquehome = findViewById(R.id.shopique);
        ImageView shopiquegrocery = findViewById(R.id.shopiquegrow);
        shopiquehome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
        });
        shopiquegrocery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Grocery.class);
            startActivity(intent);
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);

        bottomNavigationView.setOnItemSelectedListener(menuItem ->{
            handleNavigation(menuItem.getItemId());
            return true;
        });
    }

    private void handleNavigation(int menuItemId) {
        Intent intent = null;

        if (menuItemId == R.id.bottom_nav_home) {
            // Stay on the current shweta
            return;
        } else if (menuItemId == R.id.bottom_nav_categories) {
            intent = new Intent(getApplicationContext(), Categories.class);
        } else if (menuItemId == R.id.bottom_nav_Profile) {
            intent = new Intent(getApplicationContext(), Profile.class);
        } else if (menuItemId == R.id.bottom_nav_cart) {
            intent = new Intent(getApplicationContext(), Cart.class);
        }

        if (intent != null) {
            startActivity(intent);
            ActivityUtils.applyTransition(MainActivity.this); // Apply transition
            finish();
        }
    }
}