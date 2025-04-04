package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPverificationActivity extends AppCompatActivity {

    private TextView phoneNo, textView35;
    private EditText otp;
    private Button verify;
    private int OTP_number;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        phoneNo = findViewById(R.id.phone_no);
        otp = findViewById(R.id.otp);
        verify = findViewById(R.id.verify_btn);
        textView35 = findViewById(R.id.textView35);

        // Get the phone number from the intent
        String userNo = getIntent().getStringExtra("mobileNo");
        if (userNo != null && !userNo.isEmpty()) {
            phoneNo.setText("Verification code has been sent to " + userNo);
        } else {
            phoneNo.setText("Verification code has been sent to your registered number.");
        }

        // Generate a random OTP number
        OTP_number = new Random().nextInt(900000) + 100000;
        Log.d("Generated OTP", String.valueOf(OTP_number));

        // Display the OTP temporarily for testing (remove in production)
        textView35.setText("Your OTP is: " + OTP_number);

        // Generate a random order ID
        orderId = "ORDER" + (new Random().nextInt(900000) + 100000);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = otp.getText().toString().trim();
                if (enteredOtp.equals(String.valueOf(OTP_number))) {

                    Map<String,Object> updateStatus = new HashMap<>();
                    updateStatus.put("Order_Status","Ordered");
                    String s = getIntent().getStringExtra("ORDER_ID");
                    FirebaseFirestore.getInstance().collection("ORDERS").document(s).update(updateStatus)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Map<String,Object> userOrder = new HashMap<>();
                                        userOrder.put("ORDER_ID",s);
                                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(s)
                                                .set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            DeliveryActivity.codOrderConfirmed = true;
                                                            sendOrderConfirmationMessage(orderId);
                                                            // Sending the order ID to DeliveryActivity
                                                            Intent intent = new Intent(OTPverificationActivity.this, DeliveryActivity.class);
                                                            intent.putExtra("orderId", orderId); // Pass the generated order ID
                                                            startActivity(intent);
                                                            finish();
                                                            Toast.makeText(OTPverificationActivity.this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(OTPverificationActivity.this, "failed to update user order list", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    }else {
                                        Toast.makeText(OTPverificationActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(OTPverificationActivity.this, "Incorrect OTP!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void sendOrderConfirmationMessage(String orderId) {
        String message = "Your order " + orderId + " has been successfully confirmed. Thank you for shopping with us!";

        FirebaseMessaging.getInstance().subscribeToTopic("order_confirmation")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Log the message or send it to the server
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to send confirmation", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
