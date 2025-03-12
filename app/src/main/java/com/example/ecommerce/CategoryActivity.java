package com.example.ecommerce;

import static com.example.ecommerce.DBqueries.lists;
import static com.example.ecommerce.DBqueries.loadFragmentData;
import static com.example.ecommerce.DBqueries.loadedCategoriesNames;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {


    private RecyclerView categoryRecyclerView;
    private HomePageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryRecyclerView = findViewById(R.id.category_recyclerview);
        //////////////// testing recyclerview
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);
        HomePageAdapter adapter;
        int listPosition = 0;
        for (int x = 0; x < loadedCategoriesNames.size();x++){
            if (loadedCategoriesNames.get(x).equals(title.toUpperCase())){
                listPosition = x;
            }
        }
        if (listPosition == 0){
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            adapter = new HomePageAdapter(lists.get(loadedCategoriesNames.size() -1));
            loadFragmentData(adapter,this,loadedCategoriesNames.size() -1,title);
        }else {
            adapter = new HomePageAdapter(lists.get(listPosition));
        }


        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.main_search_icon) {
            //todo: search
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
            
        }

        return super.onOptionsItemSelected(item);
    }
}