package com.example.ecommerce;

import static com.example.ecommerce.DeliveryActivity.SELECT_ADDRESS;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MyAddressesActivity extends AppCompatActivity {

    private static AddressesAdapter addressesAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_addresses);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView myAddressesRecyclerView = findViewById(R.id.addresses_recyclerview);
        Button deliverHereBtn = findViewById(R.id.deliver_here_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(layoutManager);

        List<AddressesModel> addressesModelList = new ArrayList<>();
        addressesModelList.add(new AddressesModel("Usha ","Parvat patiya","395010",true));
        addressesModelList.add(new AddressesModel("Eshika ","Parvat patiya","395010",false));
        addressesModelList.add(new AddressesModel("Shweta ","Parvat patiya","395010",false));
        addressesModelList.add(new AddressesModel("Dev ","Parvat patiya","395010",false));
        addressesModelList.add(new AddressesModel("Divya ","Parvat patiya","395010",false));
        addressesModelList.add(new AddressesModel("Shiv ","Parvat patiya","395010",false));
        addressesModelList.add(new AddressesModel("Mahakal ","Parvat patiya","395010",false));

        int mode = getIntent().getIntExtra("MODE",-1);
        if (mode == SELECT_ADDRESS){
            deliverHereBtn.setVisibility(View.VISIBLE);
        }else {
            deliverHereBtn.setVisibility(View.INVISIBLE);
        }

         addressesAdapter = new AddressesAdapter(addressesModelList,mode);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator) Objects.requireNonNull(myAddressesRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();
    }


    public static  void refreshItem(int deselect, int select){
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}