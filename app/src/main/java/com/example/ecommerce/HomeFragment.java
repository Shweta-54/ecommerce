package com.example.ecommerce;

import static com.example.ecommerce.DBqueries.categoryModelList;
import static com.example.ecommerce.DBqueries.lists;
import static com.example.ecommerce.DBqueries.loadCategories;
import static com.example.ecommerce.DBqueries.loadFragmentData;
import static com.example.ecommerce.DBqueries.loadedCategoriesNames;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/** @noinspection ALL*/
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ImageView noInternetConnection = view.findViewById(R.id.no_internet_connection);

     ConnectivityManager connectivityManager = (ConnectivityManager)  requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
     NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();
     if (networkInfo != null && networkInfo.isConnected()) {
         noInternetConnection.setVisibility(View.GONE);

         RecyclerView categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
         LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
         layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
         categoryRecyclerView.setLayoutManager(layoutManager);


         CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
         categoryRecyclerView.setAdapter(categoryAdapter);

         if (categoryModelList.isEmpty()){
             loadCategories(categoryAdapter,getContext());
         }else {
             categoryAdapter.notifyDataSetChanged();
         }
         ///////// Banner Slider

         ///////// Banner Slider


//        ///////// Horizontal Product layout
//
//        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh14,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh15,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh16,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh17,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh18,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh19,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh20,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh21,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh22,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh23,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh25,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh26,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh27,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh28,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.sh28,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
//
//        ///////// Horizontal Product layout


         //////////////// testing recyclerview
         RecyclerView homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
         LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
         testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
         homePageRecyclerView.setLayoutManager(testingLayoutManager);


         HomePageAdapter adapter;
         if (lists.isEmpty()){
             loadedCategoriesNames.add("Home");
             lists.add(new ArrayList<>());
             adapter = new HomePageAdapter(lists.get(0));
             loadFragmentData(adapter,getContext(),0,"Home");

         }else {
             adapter = new HomePageAdapter(lists.get(0));
             adapter.notifyDataSetChanged();
         }

         homePageRecyclerView.setAdapter(adapter);
     }else {
         Glide.with(this).load(R.drawable.nointernetconnection).into(noInternetConnection);
         noInternetConnection.setVisibility(View.VISIBLE);
     }


     //////////////// testing recyclerview
     return view;
    }
}