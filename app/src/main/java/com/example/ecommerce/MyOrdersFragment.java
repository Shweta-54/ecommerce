package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyOrdersFragment extends Fragment {


    public MyOrdersFragment() {
        // Required empty public constructor
    }

    public static MyOrderAdapter myOrderAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        RecyclerView myordersRecyclerView = view.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myordersRecyclerView.setLayoutManager(layoutManager);


        myOrderAdapter = new MyOrderAdapter(DBqueries.myOrderItemModelList);
        myordersRecyclerView.setAdapter(myOrderAdapter);
        if (DBqueries.myOrderItemModelList.size() == 0) {
            DBqueries.loadOrders(getContext(),myOrderAdapter);
        }
        return view;
    }
}