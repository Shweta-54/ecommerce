package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MyCartFragment extends Fragment {


    public MyCartFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);


        RecyclerView cartItemsRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        Button continueBtn = view.findViewById(R.id.cart_continue_btn);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);


        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.drawable.sh14,"Pixcel 2",2,"Rs.49999/-","Rs.59999/-",1,0,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.sh15,"Pixcel 2",0,"Rs.49999/-","Rs.59999/-",1,1,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.sh16,"Pixcel 2",2,"Rs.49999/-","Rs.59999/-",1,2,0));
        cartItemModelList.add(new CartItemModel(1,"Price (3 items)","Rs.169999/-","Free","Rs.169999/-","Rs.5999/-"));

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        continueBtn.setOnClickListener(v -> {
            Intent  deliverIntent = new Intent(getContext(), AddAddressActivity.class);
            requireContext().startActivity(deliverIntent);
        });
        return view;
    }
}