
package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.ecommerce.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ViewPager productImagesViewpager;
    private TabLayout viewpagerIndicator;
    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    private FloatingActionButton addToWishlistBtn;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTablayout;

    //////rating layout
    private LinearLayout rateNowCantainer;
    //////rating layout

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        productImagesViewpager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishlistBtn = findViewById(R.id.add_to_wishList_btn);
        productDetailsTablayout = findViewById(R.id.product_details_tablayout);
        productDetailsViewpager = findViewById(R.id.product_details_viewpager);

        List<Integer> productImages = new ArrayList<>();
        productImages.add(R.drawable.rice);
        productImages.add(R.drawable.atta);
        productImages.add(R.drawable.grocery1);
        productImages.add(R.drawable.grocery);

        ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
        productImagesViewpager.setAdapter(productImagesAdapter);

        viewpagerIndicator.setupWithViewPager(productImagesViewpager, true);

        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ALREADY_ADDED_TO_WISHLIST) {
                    ALREADY_ADDED_TO_WISHLIST = false;
                    addToWishlistBtn.setSupportBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                } else {
                    ALREADY_ADDED_TO_WISHLIST = true;
                    addToWishlistBtn.setSupportBackgroundTintList(getResources().getColorStateList(R.color.colorRed));
                }
            }
        });

        productDetailsViewpager.setAdapter(new productDetailsAdapter(getSupportFragmentManager(), productDetailsTablayout.getTabCount()));
        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));

        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        //////rating layout
        rateNowCantainer = findViewById(R.id.rate_now_container);
        for (int x = 0;x < rateNowCantainer.getChildCount();x++){
            final int starPosition = x;
            rateNowCantainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setReting(starPosition);
                }
            });
        }
        //////rating layout
    }

    private void setReting(int starPosition) {
        for (int x = 0;x < rateNowCantainer.getChildCount();x++){
            ImageView starBtn = (ImageView)rateNowCantainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
