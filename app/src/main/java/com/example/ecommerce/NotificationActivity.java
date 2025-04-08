package com.example.ecommerce;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static  NotificationAdapter adapter;
    private boolean runQurey = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NotificationAdapter(DBqueries.notificationModelList);
        recyclerView.setAdapter(adapter);


        Map<String, Object> readMap = new HashMap<>();
        for (int x = 0; x < DBqueries.notificationModelList.size(); x++) {
            if (!DBqueries.notificationModelList.get(x).isReaded()) {
                runQurey = true;
            }
            readMap.put("Readed_" + x, true);
        }
        if (runQurey){
            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_NOTIFICATIONS")
                    .update(readMap);
        }
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int x = 0; x < DBqueries.notificationModelList.size(); x++) {
            DBqueries.notificationModelList.get(x).setReaded(true);

        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}