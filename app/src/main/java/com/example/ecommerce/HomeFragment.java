package com.example.ecommerce;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;


    ///////// Banner Slider
    private ViewPager bannerSliderViewPager;
    private List<SliderModel> sliderModelList;
    private int currentPage = 2;
    private Timer timer;

    final private long DEALY_TIME = 3000;
    final private long PERIOD_TIME = 3000;
    ///////// Banner Slider

    ///////// Strip Ad
    private ImageView stripAdImage;
    private ConstraintLayout stripAdContainer;
    ///////// Strip Ad

    ///////// Horizontal Product layout
    private TextView horizontallayoutTitle;
    private Button horizontalviewAllBtn;
    private RecyclerView horizontalRecyclerView;
    ///////// Horizontal Product layout



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
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

        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        ///////// Banner Slider

        bannerSliderViewPager = view.findViewById(R.id.banner_slider_view_pager);
        sliderModelList = new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.mipmap.profile,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.green_email,"#077AE4"));

        sliderModelList.add(new SliderModel(R.mipmap.error_icon,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.facebook,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.profile,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.google,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.green_email,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.profile,"#077AE4"));

        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.green_email,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.error_icon,"#077AE4"));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
        bannerSliderViewPager.setAdapter(sliderAdapter);
        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);

        bannerSliderViewPager.setCurrentItem(currentPage);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == ViewPager.SCROLL_STATE_IDLE){
                    pageLooper();
                }
            }
        };
        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
        startBannerSlideShow();

        bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pageLooper();
                stopBannerSliderShow();
                if (event.getAction() == MotionEvent.ACTION_UP){
                    stopBannerSliderShow();
                }
                return false;
            }
        });
        ///////// Banner Slider

        ///////// Strip Ad
        stripAdImage = view.findViewById(R.id.strip_ad_image);
        stripAdContainer = view.findViewById(R.id.strip_ad_container);

        stripAdImage.setImageResource(R.drawable.stripbanner);
        stripAdContainer.setBackgroundColor(Color.parseColor("#ffffff"));
        ///////// Strip Ad

        ///////// Horizontal Product layout
        horizontallayoutTitle = view.findViewById(R.id.horizontal_scroll_layout_title);
        horizontalviewAllBtn =  view.findViewById(R.id.horizontal_scroll_view_all_btn);
        horizontalRecyclerView = view.findViewById(R.id.horizontal_scroll_layout_recyclerview);

        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_phone_android_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_menu_camera,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_home_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_email_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_notifications_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_star_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_video_call_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.baseline_phone_android_24,"Redmi 5A","SD 625 Processor","Rs.5999/-"));


        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        horizontalRecyclerView.setLayoutManager(linearLayoutManager);

        horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
        horizontalProductScrollAdapter.notifyDataSetChanged();

        ///////// Horizontal Product layout
        return view;
    }

    ///////// Banner Slider



    private void pageLooper(){
        if (currentPage == sliderModelList.size() - 2){
            currentPage = 2;
            bannerSliderViewPager.setCurrentItem(currentPage,false);
        }
        if (currentPage == 1){
            currentPage = sliderModelList.size() - 3;
            bannerSliderViewPager.setCurrentItem(currentPage,false);
        }

    }
    private void startBannerSlideShow(){
        Handler handler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage >= sliderModelList.size()){
                    currentPage = 1;
                }
          bannerSliderViewPager.setCurrentItem(currentPage++,true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },DEALY_TIME,PERIOD_TIME);
    }
    private void stopBannerSliderShow(){
        timer.cancel();
    }

    ///////// Banner Slider
}