package com.example.ecommerce;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // ✅ Initialize DrawerLayout and NavigationView
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // ✅ Setup ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, binding.appBarMain.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // ✅ Handle navigation item selection
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);//from shweta 14
        navigationView.getMenu().getItem(0).setChecked(true);// from shweta 14

        frameLayout = findViewById(R.id.main_framlayout);
        // Set default fragment
        if (savedInstanceState == null) {
            setFragment(new HomeFragment());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.main_search_icon) {
            //todo: search
            return true;
        }else if (id == R.id.main_notification_icon){
            //todo: notification
            return true;
        }else if (id == R.id.main_cart_icon) {
            //todo: cart
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatemenWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_my_mall){

        } else if (id == R.id.nav_my_orders) {

        } else if (id == R.id.nav_my_rewards) {

        } else if (id == R.id.nav_my_cart) {

        } else if (id == R.id.nav_my_wishlist) {

        } else if (id == R.id.nav_my_account) {

        }else if (id == R.id.nav_sign_out){

        }

        // ✅ Close the drawer after selecting an item
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

}