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

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView testing;

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


        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryAdapter.notifyDataSetChanged();

        ///////// Banner Slider
        List<SliderModel> sliderModelList = new ArrayList<SliderModel>();
        sliderModelList.add(new SliderModel(R.mipmap.green_email,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.error_icon,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.facebook,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.profile,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.google,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.green_email,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.profile,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"#077AE4"));
        ///////// Banner Slider


        ///////// Horizontal Product layout

        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_phone_android_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_menu_camera,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_home_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_email_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_notifications_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_star_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_video_call_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_phone_android_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_phone_android_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_menu_camera,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_home_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_email_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_notifications_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_star_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_video_call_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_phone_android_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));

        ///////// Horizontal Product layout


        //////////////// testing recyclerview
        testing = view.findViewById(R.id.home_page_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
        testing.setLayoutManager(testingLayoutManager);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageModelList.add(new HomePageModel(0,sliderModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.stripbanner,"#ff0000"));
        homePageModelList.add(new HomePageModel(2,"Deals of the Day!",horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(3,"Deals of the Day!",horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.banner,"#000000"));
        homePageModelList.add(new HomePageModel(3,"Deals of the Day!",horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(2,"Deals of the Day!",horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(1,R.drawable.stripbanner,"#ff0000"));
        homePageModelList.add(new HomePageModel(0,sliderModelList));


        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);
        testing.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //////////////// testing recyclerview

        return view;
    }
}