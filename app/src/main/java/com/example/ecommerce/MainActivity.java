package com.example.ecommerce;


import static com.example.ecommerce.Login.setSignupFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/** @noinspection ALL*/
public class MainActivity extends AppCompatActivity {

    public static DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;




    private FrameLayout frameLayout;
    private int currentFragment = -1;
    private TextView actionbarlogo;

    private static final int REWARDS_FRAGMENT = 4;

    private static final int ACCOUNT_FRAGMENT = 5;
    public static Boolean showCart = false;


    private Window window;
    private Toolbar toolbar;
    private Dialog logInDialog;
    private FirebaseUser currentUser;
   // public static DrawerLayout drawer;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        com.example.ecommerce.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        actionbarlogo = findViewById(R.id.actionbar_logo);
        frameLayout = findViewById(R.id.main_framlayout);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // ✅ Handle navigation item selection
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.getMenu().getItem(0).setChecked(true);


        if (savedInstanceState == null)
            if (showCart) {
                drawerLayout.setDrawerLockMode(1);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                gotoFragment("My Cart", new MyCartFragment(), -2);
            } else {
                setFragment(new HomeFragment(), HOME_FRAGMENT);
            }


        logInDialog = new Dialog(MainActivity.this);
        logInDialog.setContentView(R.layout.log_in_dialog);
        logInDialog.setCancelable(true);
        logInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogLogInBtn = logInDialog.findViewById(R.id.login_btn);
        Button dialogSignupBtn = logInDialog.findViewById(R.id.signup_btn);

        // todo : loginintent = registeractivity
        Intent loginIntent = new Intent(MainActivity.this,Login.class);
        dialogLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginTabFragment.disableCloseBtn = true;
                SignupTabFragment.disableCloseBtn = true;
                logInDialog.dismiss();
                setSignupFragment = false;
                startActivity(loginIntent);
            }
        });

        dialogSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginTabFragment.disableCloseBtn = true;
                SignupTabFragment.disableCloseBtn = true;
                logInDialog.dismiss();
                setSignupFragment = true;
                startActivity(loginIntent);
            }
        });
        


    }

    @Override
    protected void onStart() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onStart();
        if (currentUser == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size() -1).setEnabled(false);
        }else{
            navigationView.getMenu().getItem(navigationView.getMenu().size() -1).setEnabled(true);
        }
    }

    public void onBackPressed() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
                super.onBackPressed();
            }else {
                if (showCart){
                    showCart = false;
                    finish();

                }else {
                    actionbarlogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);
        }
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

          Dialog signInDialog = new Dialog(MainActivity.this);
          signInDialog.setContentView(R.layout.log_in_dialog);
          if(currentUser == null){
                logInDialog.show();
            }else {
            gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
            }
            return true;
        }else if (id == android.R.id.home){
            if(showCart){
                showCart = false;
                finish();
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title,Fragment fragment,int fragmentNo) {
        actionbarlogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment,fragmentNo);
        if (fragmentNo == CART_FRAGMENT) {
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }

    @SuppressWarnings("StatemenWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (currentUser !=null) {
            int id = item.getItemId();
            if (id == R.id.nav_my_mall) {
                actionbarlogo.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
                setFragment(new HomeFragment(), HOME_FRAGMENT);
            } else if (id == R.id.nav_my_orders) {
                gotoFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);

            } else if (id == R.id.nav_my_rewards) {
                gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);

            } else if (id == R.id.nav_my_cart) {
                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            } else if (id == R.id.nav_my_wishlist) {
                gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);
            } else if (id == R.id.nav_my_account) {
                gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
            } else if (id == R.id.nav_sign_out) {
                   FirebaseAuth.getInstance().signOut();
                   DBqueries.clearData();
                   Intent registerIntent = new Intent(MainActivity.this,Login.class);
                   startActivity(registerIntent);
                   finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }else {
            drawerLayout.closeDrawer(GravityCompat.START);
            logInDialog.show();
            return false;
        }


    }

    private void setFragment(Fragment fragment,int fragmentNo){
        if (fragmentNo != currentFragment) {
            if (fragmentNo == REWARDS_FRAGMENT){
                window.setStatusBarColor(Color.parseColor("#5B04B1"));
                toolbar.setBackgroundColor(Color.parseColor("#5B04B1"));
            }else{
                window.setStatusBarColor(getResources().getColor(R.color.lavender));
                toolbar.setBackgroundColor(getResources().getColor(R.color.lavender));
            }
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }

}