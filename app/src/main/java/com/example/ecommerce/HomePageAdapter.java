package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private int lastPosition = -1;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.STRIP_AD_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return HomePageModel.GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View bannerSliderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sliding_ad_layout, viewGroup, false);
                return new BannerSliderViewholder(bannerSliderView);

            case HomePageModel.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.strip_ad_layout, viewGroup, false);
                return new StripAdBannerViewholder(stripAdView);

            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizontalProductView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout, viewGroup, false);
                return new HorizontalProductViewholder(horizontalProductView);

            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridProductView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_product_layout, viewGroup, false);
                return new GridProductViewholder(gridProductView);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER:
                ((BannerSliderViewholder) viewHolder).setBannerSliderViewPager(homePageModelList.get(position).getSliderModelList());
                break;

            case HomePageModel.STRIP_AD_BANNER:
                ((StripAdBannerViewholder) viewHolder).setStripAd(
                        homePageModelList.get(position).getResource(),
                        homePageModelList.get(position).getBackgroundColor()
                );
                break;

            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                ((HorizontalProductViewholder) viewHolder).setHorizontalProductLayout(
                        homePageModelList.get(position).getHorizontalProductScrollModelList(),
                        homePageModelList.get(position).getTitle(),
                        homePageModelList.get(position).getBackgroundColor(),
                        homePageModelList.get(position).getViewAllProductList()
                );
                break;

            case HomePageModel.GRID_PRODUCT_VIEW:
                ((GridProductViewholder) viewHolder).setGridProductLayout(
                        homePageModelList.get(position).getHorizontalProductScrollModelList(),
                        homePageModelList.get(position).getTitle(),
                        homePageModelList.get(position).getBackgroundColor()
                );
                break;
        }

        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    // ---------------------------- BANNER SLIDER ----------------------------
    public class BannerSliderViewholder extends RecyclerView.ViewHolder {
        private ViewPager bannerSliderViewPager;
        private int currentPage;
        private Timer timer;

        final private long DEALY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        private List<SliderModel> arrangedList;

        public BannerSliderViewholder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.banner_slider_view_pager);
        }

        private void setBannerSliderViewPager(List<SliderModel> sliderModelList) {
            currentPage = 2;
            if (timer != null) {
                timer.cancel();
            }
            arrangedList = new ArrayList<>();
            for (int x = 0; x < sliderModelList.size(); x++) {
                arrangedList.add(sliderModelList.get(x));
            }
            arrangedList.add(0, sliderModelList.get(sliderModelList.size() - 2));
            arrangedList.add(1, sliderModelList.get(sliderModelList.size() - 1));
            arrangedList.add(sliderModelList.get(0));
            arrangedList.add(sliderModelList.get(1));

            SliderAdapter sliderAdapter = new SliderAdapter(arrangedList);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);
            bannerSliderViewPager.setCurrentItem(currentPage);

            bannerSliderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {}

                @Override
                public void onPageSelected(int i) {
                    currentPage = i;
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                    if (i == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(arrangedList);
                    }
                }
            });

            bannerSliderViewPager.setOnTouchListener((v, event) -> {
                pageLooper(arrangedList);
                stopBannerSliderShow();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startBannerSlideShow(arrangedList);
                }
                return false;
            });

            startBannerSlideShow(arrangedList);
        }

        private void pageLooper(List<SliderModel> sliderModelList) {
            if (currentPage == sliderModelList.size() - 2) {
                currentPage = 2;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1) {
                currentPage = sliderModelList.size() - 3;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
        }

        private void startBannerSlideShow(List<SliderModel> sliderModelList) {
            Handler handler = new Handler();
            Runnable update = () -> {
                if (currentPage >= sliderModelList.size()) {
                    currentPage = 1;
                }
                bannerSliderViewPager.setCurrentItem(currentPage++, true);
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DEALY_TIME, PERIOD_TIME);
        }

        private void stopBannerSliderShow() {
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    // ---------------------------- STRIP AD ----------------------------
    public class StripAdBannerViewholder extends RecyclerView.ViewHolder {
        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        public StripAdBannerViewholder(@NonNull View itemView) {
            super(itemView);
            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
        }

        private void setStripAd(String resource, String color) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.banner_placeholder)).into(stripAdImage);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    // ---------------------------- HORIZONTAL PRODUCT VIEW ----------------------------
    public class HorizontalProductViewholder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private TextView horizontalLayoutTitle;
        private Button horizontalLayoutViewAllBtn;
        private RecyclerView horizontalRecyclerView;

        public HorizontalProductViewholder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalLayoutViewAllBtn = itemView.findViewById(R.id.horizontal_scroll_view_all_btn);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recyclerview);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);
        }

        private void setHorizontalProductLayout(List<HorizontalProductScrollModel> productList, String title, String color, List<WishlistModel> viewAllList) {
            try {
                if (color != null && !color.trim().isEmpty()) {
                    container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                } else {
                    container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff"))); // default fallback
                }
            } catch (IllegalArgumentException e) {
                container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff"))); // fallback on invalid format
            }            horizontalLayoutTitle.setText(title);

            for (HorizontalProductScrollModel model : productList) {
                if (!model.getProductID().isEmpty() && model.getProductTitle().isEmpty()) {
                    firebaseFirestore.collection("PRODUCTS").document(model.getProductID()).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            model.setProductTitle(task.getResult().getString("product_title"));
                            model.setProductImage(task.getResult().getString("product_image_1"));
                            model.setProductPrice(String.valueOf(task.getResult().get("product_price")));

                            WishlistModel wishlistModel = viewAllList.get(productList.indexOf(model));
                            wishlistModel.setProductTitle(model.getProductTitle());
                            wishlistModel.setProductImage(model.getProductImage());
                            wishlistModel.setProductPrice(model.getProductPrice());
                            wishlistModel.setTotalRatings(task.getResult().getLong("total_ratings"));
                            wishlistModel.setRating(task.getResult().getString("average_rating"));
                            wishlistModel.setFreeCoupens(task.getResult().getLong("free_coupens"));
                            wishlistModel.setCuttedPrice(String.valueOf(task.getResult().get("cutted_price")));
                            wishlistModel.setCOD(task.getResult().getBoolean("COD"));
                            wishlistModel.setInStock(task.getResult().getLong("stock_quantity") > 0);

                            if (productList.indexOf(model) == productList.size() - 1 && horizontalRecyclerView.getAdapter() != null) {
                                horizontalRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                    });
                }
            }

            if (productList.size() > 8) {
                horizontalLayoutViewAllBtn.setVisibility(View.VISIBLE);
                horizontalLayoutViewAllBtn.setOnClickListener(v -> {
                    ViewAllActivity.wishlistModelList = viewAllList;
                    Intent intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                    intent.putExtra("layout_code", 0);
                    intent.putExtra("title", title);
                    itemView.getContext().startActivity(intent);
                });
            } else {
                horizontalLayoutViewAllBtn.setVisibility(View.INVISIBLE);
            }

            HorizontalProductScrollAdapter adapter = new HorizontalProductScrollAdapter(productList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false);
            horizontalRecyclerView.setLayoutManager(layoutManager);
            horizontalRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    // ---------------------------- GRID PRODUCT VIEW ----------------------------
    public class GridProductViewholder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private TextView gridLayoutTitle;
        private Button gridLayoutViewAllBtn;
        private GridLayout gridProductLayout;

        public GridProductViewholder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutViewAllBtn = itemView.findViewById(R.id.grid_product_layout_viewall_btn);
            gridProductLayout = itemView.findViewById(R.id.grid_layout);
        }

        private void setGridProductLayout(List<HorizontalProductScrollModel> productList, String title, String color) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);

            for (int x = 0; x < 4; x++) {
                View gridChild = gridProductLayout.getChildAt(x);
                ImageView productImage = gridChild.findViewById(R.id.h_s_product_image);
                TextView productTitle = gridChild.findViewById(R.id.h_s_product_title);
                TextView productDescription = gridChild.findViewById(R.id.h_s_product_describtion);
                TextView productPrice = gridChild.findViewById(R.id.h_s_product_price);

                HorizontalProductScrollModel model = productList.get(x);

                // Check if product ID is valid
                if (model.getProductID() == null || model.getProductID().isEmpty()) {
                    // Handle invalid product ID case
                    productImage.setImageResource(R.drawable.placeholder);
                    productTitle.setText("Product not available");
                    productDescription.setText("");
                    productPrice.setText("");
                    continue;
                }

                if (model.getProductImage() == null || model.getProductImage().isEmpty()
                        || model.getProductTitle() == null || model.getProductTitle().isEmpty()) {
                    // Load missing product data from Firestore
                    firebaseFirestore.collection("PRODUCTS").document(model.getProductID())
                            .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                                    // Update model and UI
                                    DocumentSnapshot document = task.getResult();
                                    model.setProductTitle(document.getString("product_title"));
                                    model.setProductDescription(document.getString("product_description"));
                                    model.setProductImage(document.getString("product_image_1"));
                                    model.setProductPrice(String.valueOf(document.get("product_price")));
                                    // Update UI
                                    Glide.with(itemView.getContext())
                                            .load(model.getProductImage())
                                            .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                                            .into(productImage);

                                    productTitle.setText(model.getProductTitle());
                                    productDescription.setText(model.getProductDescription());
                                    productPrice.setText("Rs." + model.getProductPrice() + "/-");
                                } else {
                                    // Handle error case
                                    productImage.setImageResource(R.drawable.placeholder);
                                    productTitle.setText("Product not available");
                                    productDescription.setText("");
                                    productPrice.setText("");
                                }
                            });
                } else {
                    // Use existing product data
                    Glide.with(itemView.getContext())
                            .load(model.getProductImage())
                            .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                            .into(productImage);

                    productTitle.setText(model.getProductTitle());
                    productDescription.setText(model.getProductDescription());
                    productPrice.setText("Rs." + model.getProductPrice() + "/-");
                }

                gridChild.setBackgroundColor(Color.parseColor("#ffffff"));

                int finalX = x;
                gridChild.setOnClickListener(v -> {
                    Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    intent.putExtra("PRODUCT_ID", productList.get(finalX).getProductID());
                    itemView.getContext().startActivity(intent);
                });
            }

            gridLayoutViewAllBtn.setOnClickListener(v -> {
                ViewAllActivity.horizontalProductScrollModelList = productList;
                Intent intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                intent.putExtra("layout_code", 1);
                intent.putExtra("title", title);
                itemView.getContext().startActivity(intent);
            });
        }

    }
}
