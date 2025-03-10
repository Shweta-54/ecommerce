package com.example.ecommerce;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Deals of the Day");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView1);
        gridView = findViewById(R.id.grid_view);

        int layout_code =getIntent().getIntExtra("layout_code",-1);

        if (layout_code == 0) {

            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            List<WishlistModel> wishlistModelList = new ArrayList<>();
            wishlistModelList.add(new WishlistModel(R.drawable.sh14, "Pixel 2", 1, "3", 145, "Rs.49999/-", "59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.sh15, "Pixel 2", 0, "3", 145, "Rs.49999/-", "59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.sh16, "Pixel 2", 2, "3", 145, "Rs.49999/-", "59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.sh17, "Pixel 2", 4, "3", 145, "Rs.49999/-", "59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.sh18, "Pixel 2", 1, "3", 145, "Rs.49999/-", "59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.sh19, "Pixel 2", 1, "3", 145, "Rs.49999/-", "59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.sh20, "Pixel 2", 0, "3", 145, "Rs.49999/-", "59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.sh21, "Pixel 2", 2, "3", 145, "Rs.49999/-", "59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.sh22, "Pixel 2", 4, "3", 145, "Rs.49999/-", "59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.sh23, "Pixel 2", 1, "3", 145, "Rs.49999/-", "59999/-", "Cash on delivery"));


            WishlistAdapter adapter = new WishlistAdapter(wishlistModelList, false);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (layout_code == 1) {


            gridView.setVisibility(View.VISIBLE);

            List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();


            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
            gridView.setAdapter(gridProductLayoutAdapter);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}