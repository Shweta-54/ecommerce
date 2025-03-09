package com.example.ecommerce;

import static android.os.Build.VERSION_CODES.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        final List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>(); 
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Electronics"));
        categoryModelList.add(new CategoryModel("link","Appliances"));
        categoryModelList.add(new CategoryModel("link","Furniture"));
        categoryModelList.add(new CategoryModel("link","Sports"));
        categoryModelList.add(new CategoryModel("link","Fitness"));
        categoryModelList.add(new CategoryModel("link","Women Art"));
        categoryModelList.add(new CategoryModel("link","Men Art"));
        categoryModelList.add(new CategoryModel("link","Books"));
        categoryModelList.add(new CategoryModel("link","Shoes"));
        categoryModelList.add(new CategoryModel("link","Testing"));


        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();


        //      ///////// Horizontal Product layout
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
//        /////// Horizontal Product layout


        //////////////// testing recyclerview
       homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);
       List<HomePageModel> homePageModelList = new ArrayList<>();
        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);
        homePageRecyclerView.setAdapter(adapter);



        adapter.notifyDataSetChanged();
        //////////////// testing recyclerview

        return view;
    }
}