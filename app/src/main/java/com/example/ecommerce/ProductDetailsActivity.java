package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

public class ProductDetailsActivity extends AppCompatActivity {

    public static String productID = "XMDckLqWeUdDu0WQ1yii";
    private ViewPager productImagesViewpager;
    private TextView productTitle, averageRatingMiniView, totalRatingMiniView, productPrice, cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TabLayout viewpagerIndicator;
    private Button coupenRedeemBtn;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static FloatingActionButton addToWishlistBtn;
    private static RecyclerView coupensRecyclerView;
    private static LinearLayout selectedCoupen;
    private TextView rewardTitle, rewardBody;

    // Product Description & Specifications containers
    private ConstraintLayout productDetailsOnlycontainer, productDetailsTabscontainer;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTablayout;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    private String productDescription, productOtherDetails;
    private TextView productOnlyDescriptionBody;

    // Coupen dialog views
    public static TextView coupentitle, coupenExpiryDate, coupenBody;

    // Rating layout views
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer, rateNowCantainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;

    private Button BuyNowBtn;
    private FirebaseFirestore firebaseFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Bind views
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
        rateNowCantainer = findViewById(R.id.rate_now_container);

        // Initialize Firestore and load product data
        firebaseFirestore = FirebaseFirestore.getInstance();
        List<String> productImages = new ArrayList<>();

        firebaseFirestore.collection("PRODUCTS").document(getIntent().getStringExtra("PRODUCT_ID"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Log.d("FirestoreData", "Document exists");

                                // Load product images
                                Long noOfImages = documentSnapshot.getLong("no_of_product_images");
                                if (noOfImages != null) {
                                    for (long x = 1; x <= noOfImages; x++) {
                                        String imageurl = documentSnapshot.getString("product_image_" + x);
                                        if (imageurl != null) {
                                            productImages.add(imageurl);
                                        }
                                    }
                                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                                    productImagesViewpager.setAdapter(productImagesAdapter);
                                }

                                // Set product details
                                String title = documentSnapshot.getString("product_title");
                                if (title != null) {
                                    productTitle.setText(title);
                                } else {
                                    productTitle.setText("Product Title Not Available");
                                }

                                Long averageRatingValue = documentSnapshot.getLong("average_rating");
                                if (averageRatingValue != null) {
                                    averageRatingMiniView.setText(String.valueOf(averageRatingValue));
                                } else {
                                    averageRatingMiniView.setText("0");
                                }

                                Long totalRatingsValue = documentSnapshot.getLong("total_ratings");
                                if (totalRatingsValue != null) {
                                    totalRatingMiniView.setText("(" + totalRatingsValue + ") ratings");
                                } else {
                                    totalRatingMiniView.setText("(0) ratings");
                                }

                                Long productPriceValue = documentSnapshot.getLong("product_price");
                                if (productPriceValue != null) {
                                    productPrice.setText("Rs." + productPriceValue + "/-");
                                } else {
                                    productPrice.setText("Rs. 0/-");
                                }

                                Long cuttedPriceValue = documentSnapshot.getLong("cutted_price");
                                if (cuttedPriceValue != null) {
                                    cuttedPrice.setText("Rs." + cuttedPriceValue + "/-");
                                } else {
                                    cuttedPrice.setText("Rs. 0/-");
                                }

                                // COD indicator
                                Boolean cod = documentSnapshot.getBoolean("COD");
                                if (cod != null && cod) {
                                    codIndicator.setVisibility(View.VISIBLE);
                                    tvCodIndicator.setVisibility(View.VISIBLE);
                                } else {
                                    codIndicator.setVisibility(View.INVISIBLE);
                                    tvCodIndicator.setVisibility(View.INVISIBLE);
                                }

                                // Reward details
                                Long freeCoupens = documentSnapshot.getLong("free_coupens");
                                String freeCoupenTitle = documentSnapshot.getString("free_coupen_title");
                                String freeCoupenBody = documentSnapshot.getString("free_coupen_body");
                                if (freeCoupens != null && freeCoupenTitle != null && freeCoupenBody != null) {
                                    rewardTitle.setText(freeCoupens + " " + freeCoupenTitle);
                                    rewardBody.setText(freeCoupenBody);
                                } else {
                                    rewardTitle.setText("0 Rewards");
                                    rewardBody.setText("No rewards available");
                                }

                                // Handle product description/specifications
                                Boolean useTabLayout = documentSnapshot.getBoolean("use_tab_layout");
                                if (useTabLayout != null) {
                                    if (useTabLayout) {
                                        productDetailsTabscontainer.setVisibility(View.VISIBLE);
                                        productDetailsOnlycontainer.setVisibility(View.GONE);
                                        productDescription = documentSnapshot.getString("product_description");
                                        productOtherDetails = documentSnapshot.getString("product_other_details");
                                        Long totalSpecTitles = documentSnapshot.getLong("total_spec_titles");
                                        if (totalSpecTitles != null) {
                                            for (long x = 1; x <= totalSpecTitles; x++) {
                                                String specTitle = documentSnapshot.getString("spec_title_" + x);
                                                if (specTitle != null) {
                                                    productSpecificationModelList.add(new ProductSpecificationModel(0, specTitle));
                                                    Long totalFields = documentSnapshot.getLong("spec_title_" + x + "_total_fields");
                                                    if (totalFields != null) {
                                                        for (long y = 1; y <= totalFields; y++) {
                                                            String fieldName = documentSnapshot.getString("spec_title_" + x + "_field_" + y + "_name");
                                                            String fieldValue = documentSnapshot.getString("spec_title_" + x + "_field_" + y + "_value");
                                                            if (fieldName != null && fieldValue != null) {
                                                                productSpecificationModelList.add(new ProductSpecificationModel(1, fieldName, fieldValue));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        productDetailsTabscontainer.setVisibility(View.GONE);
                                        productDetailsOnlycontainer.setVisibility(View.VISIBLE);
                                        String description = documentSnapshot.getString("product_description");
                                        if (description != null) {
                                            productOnlyDescriptionBody.setText(description);
                                        } else {
                                            productOnlyDescriptionBody.setText("No description available");
                                        }
                                    }
                                }

                                // Ratings
                                Long totalRatingsValue1 = documentSnapshot.getLong("total_ratings");
                                if (totalRatingsValue1 != null) {
                                    totalRatings.setText(totalRatingsValue1 + " ratings");
                                    for (int x = 0; x < 5; x++) {
                                        TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                                        Long starRating = documentSnapshot.getLong((5 - x) + "_star");
                                        if (starRating != null) {
                                            rating.setText(String.valueOf(starRating));
                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                            progressBar.setMax(totalRatingsValue1.intValue());
                                            progressBar.setProgress(starRating.intValue());
                                        }
                                    }
                                    totalRatingsFigure.setText(String.valueOf(totalRatingsValue1));
                                } else {
                                    totalRatings.setText("0 ratings");
                                    totalRatingsFigure.setText("0");
                                }

                                // Setup ViewPager for product details tabs
                                productDetailsViewpager.setAdapter(new productDetailsAdapter(
                                        getSupportFragmentManager(),
                                        productDetailsTablayout.getTabCount(),
                                        productDescription,
                                        productOtherDetails,
                                        productSpecificationModelList
                                ));
                            } else {
                                Log.d("FirestoreData", "Document does not exist");
                                Toast.makeText(ProductDetailsActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Log.e("FirestoreData", "Error fetching document: " + error);
                            Toast.makeText(ProductDetailsActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Setup viewpager indicator
        viewpagerIndicator.setupWithViewPager(productImagesViewpager, true);

        // Wishlist button click
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

        // Setup tab layout with product details ViewPager
        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });

        // Rating stars click listeners
        for (int x = 0; x < rateNowCantainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowCantainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setReting(starPosition);
                }
            });
        }

        // Buy Now button click
        BuyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                startActivity(deliveryIntent);
            }
        });

        // Coupen dialog setup
        final Dialog checkCoupenPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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

        // Sample rewards list
        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("CashBack", "till 2nd,June 2024", "GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));
        rewardModelList.add(new RewardModel("Discount", "till 2nd,June 2024", "GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free", "till 2nd,June 2024", "GET 20% CASHBACK on any product above Rs.200/- and Rs.3000/-."));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList, true);
        coupensRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });

        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCoupenPriceDialog.show();
            }
        });
    }

    public static void showDialogRecyclerView() {
        if (coupensRecyclerView.getVisibility() == View.GONE) {
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        } else {
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

    private void setReting(int starPosition) {
        for (int x = 0; x < rateNowCantainer.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowCantainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
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
            startActivity(cartIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}