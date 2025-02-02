package com.example.ecommerce;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

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
        shopiquehome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        shopiquegrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Grocery.class);
                startActivity(intent);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);

        bottomNavigationView.setOnItemSelectedListener(menuItem ->{
            int id =menuItem.getItemId();
            if (id == R.id.bottom_nav_home){
                return true;
            }
            if (id == R.id.bottom_nav_categories){
                startActivity(new Intent(getApplicationContext(),Categories.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            if (id == R.id.bottom_nav_Profile){
                startActivity(new Intent(getApplicationContext(), Profile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            if (id == R.id.bottom_nav_cart){
                startActivity(new Intent(getApplicationContext(),Cart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

}