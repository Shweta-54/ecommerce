package com.example.ecommerce;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        // Handle Login Button Click
       Button LoginButton = findViewById(R.id.btnLogin);
       LoginButton.setOnClickListener(view -> Toast.makeText(Account.this, "Login button is clicked", Toast.LENGTH_SHORT).show());
    }
}