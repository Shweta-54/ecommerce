package com.example.ecommerce;

import static com.example.ecommerce.DBqueries.categoryModelList;
import static com.example.ecommerce.DBqueries.lists;
import static com.example.ecommerce.DBqueries.loadCategories;
import static com.example.ecommerce.DBqueries.loadFragmentData;
import static com.example.ecommerce.DBqueries.loadedCategoriesNames;

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

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView ;
    private HomePageAdapter adapter;
    private ImageView noInternetConnection;


 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);

     ConnectivityManager connectivityManager = (ConnectivityManager)  getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
     NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();
     if (networkInfo != null && networkInfo.isConnected() == true) {
         noInternetConnection.setVisibility(View.GONE);

         categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
         LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
         layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
         categoryRecyclerView.setLayoutManager(layoutManager);


         categoryAdapter = new CategoryAdapter(categoryModelList);
         categoryRecyclerView.setAdapter(categoryAdapter);

         if (categoryModelList.size() ==  0){
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
         homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
         LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
         testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
         homePageRecyclerView.setLayoutManager(testingLayoutManager);


         if (lists.size() ==  0){
             loadedCategoriesNames.add("Home");
             lists.add(new ArrayList<HomePageModel>());
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