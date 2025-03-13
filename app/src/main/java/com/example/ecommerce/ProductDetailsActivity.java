
package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** @noinspection ALL*/
public class ProductDetailsActivity extends AppCompatActivity {

    private ViewPager productImagesViewpager;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TabLayout viewpagerIndicator;
    private Button coupenRedeemBtn;
    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    private FloatingActionButton addToWishlistBtn;
    private static RecyclerView coupensRecyclerView;
     private static LinearLayout selectedCoupen;

     private TextView rewardTitle;
     private TextView rewardBody;



/////Product Description
    private ConstraintLayout productDetailsOnlycontainer;
    private ConstraintLayout productDetailsTabscontainer;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTablayout;
    public List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    private String productDescription;
    private String productOtherDetails;
    private TextView productOnlyDescriptionBody;
    /////Product Description
    /////coupen dialog
    public static TextView coupentitle;
    public static TextView coupenExpiryDate;
    public static TextView coupenBody;


    /////coupen dialog

    //////rating layout
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private LinearLayout rateNowCantainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    //////rating layout

    private Button BuyNowBtn;
    private FirebaseFirestore firebaseFirestore;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        productImagesViewpager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishlistBtn = findViewById(R.id.add_to_wishList_btn);
        productDetailsTablayout = findViewById(R.id.product_details_tablayout);
        productDetailsViewpager = findViewById(R.id.product_details_viewpager);
        BuyNowBtn = findViewById(R.id.buy_now_btn);
        coupenRedeemBtn = findViewById(R.id.coupen_redemption_btn);
        productTitle = findViewById(R.id.product_title5);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_ratings_miniview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        codIndicator = findViewById(R.id.cod_indicator_imageview);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        productDetailsTabscontainer = findViewById(R.id.product_details_tabs_container);
        productDetailsOnlycontainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.rating_numbers_container);
        totalRatingsFigure = findViewById(R.id.total_retings_figure);
        ratingsProgressBarContainer = findViewById(R.id.rating_progressbar_container);
        averageRating = findViewById(R.id.average_rating);

        firebaseFirestore = FirebaseFirestore.getInstance(); //
       List<String> productImages = new ArrayList<>();

        firebaseFirestore.collection("PRODUCTS").document("XMDckLqWeUdDu0WQ1yii")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                             for (long x = 1;x < (long)documentSnapshot.get("no_of_product_images") + 1;x++){
                                productImages.add(documentSnapshot.get("product_image_"+x).toString());
                            }
                            ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                            productImagesViewpager.setAdapter(productImagesAdapter);

                            productTitle.setText(documentSnapshot.get("product_title").toString());
                            averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                            totalRatingMiniView.setText("("+(long) documentSnapshot.get("total_ratings")+")ratings");
                            productPrice.setText("Rs."+documentSnapshot.get("product_price").toString()+"/-");
                            cuttedPrice.setText("Rs."+documentSnapshot.get("cutted_price").toString()+"/-");
                            if ((boolean) documentSnapshot.get("COD")){
                                codIndicator.setVisibility(View.VISIBLE);
                                tvCodIndicator.setVisibility(View.VISIBLE);
                            }else {
                                codIndicator.setVisibility(View.INVISIBLE);
                                tvCodIndicator.setVisibility(View.INVISIBLE );
                            }

                            rewardTitle.setText((long) documentSnapshot.get("free_coupens") + documentSnapshot.get("free_coupen_title").toString());
                            rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());

                            if ((boolean)documentSnapshot.get("use_tab_layout")){
                                productDetailsTabscontainer.setVisibility(View.VISIBLE);
                                productDetailsOnlycontainer.setVisibility(View.GONE);
                                productDescription = documentSnapshot.get("product_description").toString();//productDescriptionFragment ki jagah pr ab jo string create kiya gaya h usko set kiya h ab
                                ////productSpecificationFragment.productspecificationModellist part 2 56 part 1 55

                                productOtherDetails = documentSnapshot.get("product_other_details").toString();

                                for (long x = 1;x < (long) documentSnapshot.get("total_spec_titles") + 1;x++){
                                    //productSpecificationFragment.productspecificationModellist.add()-----
                                            productSpecificationModelList.add(new ProductSpecificationModel(0,documentSnapshot.get("spec_title_"+x).toString()));
                                    for (long y = 1; y < (long) documentSnapshot.get("spec_title_"+x+"_total_fields")+1;y++){
                                        productSpecificationModelList.add(new ProductSpecificationModel(1,documentSnapshot.get("spec_title_"+x+"_field_"+y+"_name").toString(),documentSnapshot.get("spec_title_"+x+"_field_"+y+"_value").toString()));

                                    }
                                }

                            }else {
                                productDetailsTabscontainer.setVisibility(View.GONE);
                                productDetailsOnlycontainer.setVisibility(View.VISIBLE);
                                productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());//do jagah product description
                            }

                            totalRatings.setText((long)documentSnapshot.get("total_ratings")+"ratings");

                            for (int x = 0; x < 5;x++){
                                TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                                rating.setText(String.valueOf((long) documentSnapshot.get(5-x+"_star")));

                                ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                  int maxProgress = Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_ratings")));
                                progressBar.setMax(maxProgress);
                                progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5-x) +"_star"))));
                            }
                            totalRatingsFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")));
                            productDetailsViewpager.setAdapter(new productDetailsAdapter(getSupportFragmentManager(), productDetailsTablayout.getTabCount(),productDescription,productOtherDetails,productSpecificationModelList));
                            averageRating.setText(documentSnapshot.get("average_rating").toString());


                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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

        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));

        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // tabposition = tab.getposition(); static variable bhi create kiya h uper uske baad description fragmnet mai if wagera set kiya gaya h
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

        BuyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                startActivity(deliveryIntent);
            }
        });

        /////  coupen dialog


        Dialog checkCoupenPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        ImageView toggleRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
        coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
        selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);

        coupentitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
        coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_validity);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);

        TextView originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
        TextView discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);


        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        coupensRecyclerView.setLayoutManager(layoutManager);


        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("CashBack","till 2nd,June 2024","GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));
        rewardModelList.add(new RewardModel("Discount","till 2nd,June 2024","GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free","till 2nd,June 2024","GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));
        rewardModelList.add(new RewardModel("CashBack","till 2nd,June 2024","GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));
        rewardModelList.add(new RewardModel("Discount","till 2nd,June 2024","GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free","till 2nd,June 2024","GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));
        rewardModelList.add(new RewardModel("CashBack","till 2nd,June 2024","GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));
        rewardModelList.add(new RewardModel("Discount","till 2nd,June 2024","GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free","till 2nd,June 2024","GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));


        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList,true);
        coupensRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });
        ///// coupen dialog

        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCoupenPriceDialog.show();
            }
        });

    }

      public static  void  showDialogRecyclerView(){
        if (coupensRecyclerView.getVisibility() == View.GONE){
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        }else {
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
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
            Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
            boolean showCart = true;
            startActivity(cartIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
