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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerce.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

/** @noinspection ALL*/
public class MainActivity extends AppCompatActivity {

    public static DrawerLayout drawerLayout;
    public static MainActivity mainActivity;
    public static boolean resetMainActivity = false;
    private NavigationView navigationView;


    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;

    private CircleImageView profileView;
    private TextView fullname,email;
    private ImageView addprofileIcon;



    private FrameLayout frameLayout;
    private int currentFragment = -1;
    private TextView actionbarlogo;
    private TextView badgeCount;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;
    private static final int REWARDS_FRAGMENT = 4;

    private static final int ACCOUNT_FRAGMENT = 5;
    public static Boolean showCart = false;


    private Window window;
    private Toolbar toolbar;
    private Dialog logInDialog;
    private FirebaseUser currentUser;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionbarlogo = findViewById(R.id.actionbar_logo);
        frameLayout = findViewById(R.id.main_framlayout);

        navigationView = binding.navView;

        profileView = navigationView.getHeaderView(0).findViewById(R.id.main_profile_image);
        fullname = navigationView.getHeaderView(0).findViewById(R.id.main_fullname);
        email = navigationView.getHeaderView(0).findViewById(R.id.main_email);
        addprofileIcon = navigationView.getHeaderView(0).findViewById(R.id.add_profile_icon);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();

        drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;


        // ✅ Handle navigation item selection
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.getMenu().getItem(0).setChecked(true);

        if (savedInstanceState == null)
            if (showCart) {
                mainActivity = this;
                drawerLayout.setDrawerLockMode(1);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                gotoFragment("My Cart", new MyCartFragment(), -2);
            } else {
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawerLayout, toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();
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
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        }else{
//            DBqueries.checkNotifications(false);

            if (DBqueries.email == null) {
                FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DBqueries.fullname = task.getResult().getString("fullname");
                                    DBqueries.email = task.getResult().getString("email");
                                    DBqueries.profile = task.getResult().getString("profile");

                                    fullname.setText(DBqueries.fullname);
                                    email.setText(DBqueries.email);
                                    if (DBqueries.profile.equals("")) {

                                        addprofileIcon.setVisibility(View.VISIBLE);
                                    } else {
                                        addprofileIcon.setVisibility(View.INVISIBLE);
                                        Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.mipmap.profile_round)).into(profileView);
                                    }

                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else {
                fullname.setText(DBqueries.fullname);
                email.setText(DBqueries.email);
                if (DBqueries.profile.equals("")) {
                    profileView.setImageResource(R.mipmap.profile_round);
                    addprofileIcon.setVisibility(View.VISIBLE);
                } else {
                    addprofileIcon.setVisibility(View.INVISIBLE);
                    Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.mipmap.profile_round)).into(profileView);
                }
            }
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }

        if (resetMainActivity) {
            actionbarlogo.setVisibility(View.VISIBLE);
            resetMainActivity = false;
            setFragment(new HomeFragment(), HOME_FRAGMENT);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        invalidateOptionsMenu();
    }


    @Override
    protected void onPause() {
        super.onPause();
//        DBqueries.checkNotifications(true);
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
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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

           MenuItem cartItem = menu.findItem(R.id.main_cart_icon);
                cartItem.setActionView(R.layout.badge_layout);
                ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
                badgeIcon.setImageResource(R.drawable.ic_cart);
                 badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
                 if (currentUser != null) {
                     if (DBqueries.cartList.size() == 0) {
                         DBqueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this), false, badgeCount,new TextView(MainActivity.this));
                     } else {
                         badgeCount.setVisibility(View.VISIBLE);
                     if (DBqueries.cartList.size() < 99) {
                         badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                     } else {
                         badgeCount.setText("99");
                            }
                         }
                 }

                cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentUser == null){
                            logInDialog.show();
                        }else {
                            gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
                        }
                    }
                });
            MenuItem notifyItem = menu.findItem(R.id.main_notification_icon);
            notifyItem.setActionView(R.layout.badge_layout);
            ImageView notifyIcon = notifyItem.getActionView().findViewById(R.id.badge_icon);
            notifyIcon.setImageResource(R.drawable.baseline_notifications_24);
            TextView notifyCount = cartItem.getActionView().findViewById(R.id.badge_count);
            if(currentUser != null){
                DBqueries.checkNotifications(false,notifyCount);
            }
            notifyItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent notificationIntent = new Intent(MainActivity.this,NotificationActivity.class);
                    startActivity(notificationIntent);
                }
            });
            /////usha

        }
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.main_search_icon) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }else if (id == R.id.main_notification_icon){
           Intent notificationIntent = new Intent(this, NotificationActivity.class);
           startActivity(notificationIntent);
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


    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
        actionbarlogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();

        showCart = (fragmentNo == CART_FRAGMENT);
        setFragment(fragment, fragmentNo);

        // Update drawer indicator based on the fragment
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (showCart) {
            // Cart fragment par bhi drawer ko accessible rakhenge
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toggle.setDrawerIndicatorEnabled(true);  // Drawer icon enabled for cart
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); // Unlock drawer
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toggle.setDrawerIndicatorEnabled(true);   // Enable drawer icon for other fragments
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        if (fragmentNo == CART_FRAGMENT || showCart) {
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }


    @SuppressWarnings("StatemenWithEmptyBody")

    MenuItem menuItem;
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        menuItem = item;

        if (currentUser != null) {
            drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(@NonNull View drawerView) {
                    super.onDrawerClosed(drawerView);
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
                    drawerLayout.removeDrawerListener(this);

                }
            });
            return true;
        }else {
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