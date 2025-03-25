package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;


public class DeliveryActivity extends AppCompatActivity {

    public static final int SELECT_ADDRESS = 0;
    private Button continuetBtn;
    private ConstraintLayout orderConfirmationLayout;
    private ImageButton continueShoppingBtn;
    private TextView orderId;
    private TextView totalAmount;
    private TextView fullName;
    private TextView fullAddress;
    private TextView pincode;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");


        RecyclerView deliveryRecyclerView = findViewById(R.id.delivery_recyclerview);
        Button changeORaddNewAddressbtn = findViewById(R.id.change_or_add_address_btn);
        continuetBtn = findViewById(R.id.cart_continue_btn);
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        orderId = findViewById(R.id.order_id);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullName = findViewById(R.id.fullname_shipping_D);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);




        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);



        CartAdapter cartAdapter = new CartAdapter(DBqueries.cartItemModelList,totalAmount,false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeORaddNewAddressbtn.setVisibility(View.VISIBLE);
        changeORaddNewAddressbtn.setOnClickListener(v -> {
            Intent myAddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
            myAddressesIntent.putExtra("MODE",SELECT_ADDRESS);
            startActivity(myAddressesIntent);
        });


           }

    @Override
    protected void onStart() {
        super.onStart();
        fullName.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullname());
        fullAddress.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}