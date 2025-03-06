package com.example.ecommerce;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MyWishlistFragment extends Fragment {



    public MyWishlistFragment() {
        // Required empty public constructor
    }

    private RecyclerView wishlistRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_my_wishlist, container, false);

        wishlistRecyclerView = view.findViewById(R.id.my_wishlist_recyclerview);

        LinearLayoutManager  linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);

        List<WishlistModel> wishlistModelList = new ArrayList<>();
        wishlistModelList.add(new WishlistModel(R.drawable.baseline_phone_android_24,"Pixel 2",1,"3",145,"Rs.49999/-","59999/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.baseline_phone_android_24,"Pixel 2",0,"3",145,"Rs.49999/-","59999/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.baseline_phone_android_24,"Pixel 2",2,"3",145,"Rs.49999/-","59999/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.baseline_phone_android_24,"Pixel 2",4,"3",145,"Rs.49999/-","59999/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.baseline_phone_android_24,"Pixel 2",1,"3",145,"Rs.49999/-","59999/-","Cash on delivery"));

            WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlistModelList,true);
            wishlistRecyclerView.setAdapter(wishlistAdapter);
            wishlistAdapter.notifyDataSetChanged();
        return view;
    }
}