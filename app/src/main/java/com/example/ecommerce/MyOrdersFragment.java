package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersFragment extends Fragment {


    public MyOrdersFragment() {
        // Required empty public constructor
    }


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

        List<MyOrderItemModel> myOrderItemModelList =  new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.sh14,2,"Pixcel 2 (Black)","Deliverd on Mon,15th JAN 2013"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.sh15,1,"Pixcel 2 (Black)","Deliverd on Mon,15th JAN 2013"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.sh16,0,"Pixcel 2 (Black)","Cancelled"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.sh17,4,"Pixcel 2 (Black)","Deliverd on Mon,15th JAN 2013"));

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myordersRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();
        return view;
    }
}