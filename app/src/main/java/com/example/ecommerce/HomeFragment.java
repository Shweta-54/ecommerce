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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private List<CategoryModel> categoryModelFackList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView ;
    private List<HomePageModel> homePageModelFackList = new ArrayList<>();
    private HomePageAdapter adapter;
    private ImageView noInternetConnection;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.lavender),getContext().getResources().getColor(R.color.lavender),getContext().getResources().getColor(R.color.lavender));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);


        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);


        ////categories fack list
        categoryModelFackList.add(new CategoryModel("null",""));
        categoryModelFackList.add(new CategoryModel("",""));
        categoryModelFackList.add(new CategoryModel("",""));
        categoryModelFackList.add(new CategoryModel("",""));
        categoryModelFackList.add(new CategoryModel("",""));
        categoryModelFackList.add(new CategoryModel("",""));
        categoryModelFackList.add(new CategoryModel("",""));
        categoryModelFackList.add(new CategoryModel("",""));
        categoryModelFackList.add(new CategoryModel("",""));
        categoryModelFackList.add(new CategoryModel("",""));

        ////categories fack list

        //// home face list
        List<SliderModel> sliderModelFackList = new ArrayList<>();
        sliderModelFackList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFackList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFackList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFackList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFackList.add(new SliderModel("null","#dfdfdf"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFackList = new ArrayList<>();
        horizontalProductScrollModelFackList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFackList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFackList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFackList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFackList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFackList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFackList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFackList.add(new HomePageModel(0,sliderModelFackList));
        homePageModelFackList.add(new HomePageModel(1,"","#dfdfdf"));
        homePageModelFackList.add(new HomePageModel(2,"","#dfdfdf",horizontalProductScrollModelFackList,new ArrayList<WishlistModel>()));
        homePageModelFackList.add(new HomePageModel(3,"","#dfdfdf",horizontalProductScrollModelFackList));
        //// home face list


        categoryAdapter = new CategoryAdapter(categoryModelFackList);


        adapter = new HomePageAdapter(homePageModelFackList);


        connectivityManager = (ConnectivityManager)  getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo =  connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            noInternetConnection.setVisibility(View.GONE);

            if (categoryModelList.size() ==  0){
                loadCategories(categoryRecyclerView,getContext());
            }else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);

            if (lists.size() ==  0){
                loadedCategoriesNames.add("Home");
                lists.add(new ArrayList<HomePageModel>());

                loadFragmentData(homePageRecyclerView,getContext(),0,"Home");

            }else {
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(adapter);
        }else {
            Glide.with(this).load(R.drawable.nointernetconnection).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
        }

        //// refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);

                categoryModelList.clear();
                lists.clear();
                loadedCategoriesNames.clear();

                if (networkInfo != null && networkInfo.isConnected() == true) {
                    noInternetConnection.setVisibility(View.GONE);
                    categoryAdapter = new CategoryAdapter(categoryModelFackList);
                    adapter = new HomePageAdapter(homePageModelFackList);
                    categoryRecyclerView.setAdapter(categoryAdapter);
                    homePageRecyclerView.setAdapter(adapter);

                    loadCategories(categoryRecyclerView,getContext());
                    loadedCategoriesNames.add("Home");
                    lists.add(new ArrayList<HomePageModel>());
                    adapter = new HomePageAdapter(lists.get(0));
                    loadFragmentData(homePageRecyclerView,getContext(),0,"Home");
                }else {
                    Glide.with(getContext()).load(R.drawable.nointernetconnection).into(noInternetConnection);
                    noInternetConnection.setVisibility(View.VISIBLE);
                }
            }

        });
        /// refresh layout

        return view;
    }
}