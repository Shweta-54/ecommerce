package com.example.ecommerce;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static String email,fullname,profile;


    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();

    public static List<String> wishList = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    public static int selectedAddress = -1;
    public static List<AddressesModel> addressesModelList = new ArrayList<>();

    public static List<RewardModel> rewardModelList = new ArrayList<>();

    public static  List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();

    public static List<NotificationModel> notificationModelList = new ArrayList<>();
    public static ListenerRegistration registration;

    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {
        categoryModelList.clear();

        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadFragmentData(RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName) {
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS")
                .orderBy("index")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                Long viewTypeLong = documentSnapshot.getLong("view_type");
                                if (viewTypeLong == null) continue;

                                int viewType = viewTypeLong.intValue();

                                if (viewType == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    Long noOfBanners = documentSnapshot.getLong("no_of_banners");
                                    if (noOfBanners != null) {
                                        for (long x = 1; x <= noOfBanners; x++) {
                                            sliderModelList.add(new SliderModel(
                                                    String.valueOf(documentSnapshot.get("banner_" + x)),
                                                    String.valueOf(documentSnapshot.get("banner_" + x + "_background"))
                                            ));
                                        }
                                    }
                                    lists.get(index).add(new HomePageModel(0, sliderModelList));

                                } else if (viewType == 1) {
                                    lists.get(index).add(new HomePageModel(
                                            1,
                                            String.valueOf(documentSnapshot.get("strip_ad_banner")),
                                            String.valueOf(documentSnapshot.get("background"))
                                    ));

                                } else if (viewType == 2) {
                                    List<WishlistModel> viewAllProductList = new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();

                                    ArrayList<String> productIds = (ArrayList<String>) documentSnapshot.get("products");
                                    if (productIds != null) {
                                        for (String productId : productIds) {
                                            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(productId, "", "", "", ""));
                                            viewAllProductList.add(new WishlistModel(productId, "", "", 0, "", 0, "", "", false, false));
                                        }
                                    }

                                    lists.get(index).add(new HomePageModel(
                                            2,
                                            String.valueOf(documentSnapshot.get("layout_title")),
                                            String.valueOf(documentSnapshot.get("layout_background")),
                                            horizontalProductScrollModelList,
                                            viewAllProductList
                                    ));

                                } else if (viewType == 3) {
                                    List<HorizontalProductScrollModel> gridLayoutModelList = new ArrayList<>();
                                    ArrayList<String> productIds = (ArrayList<String>) documentSnapshot.get("products");
                                    if (productIds != null) {
                                        for (String productId : productIds) {
                                            gridLayoutModelList.add(new HorizontalProductScrollModel(productId, "", "", "", ""));
                                        }
                                    }

                                    lists.get(index).add(new HomePageModel(
                                            3,
                                            String.valueOf(documentSnapshot.get("layout_title")),
                                            String.valueOf(documentSnapshot.get("layout_background")),
                                            gridLayoutModelList
                                    ));
                                }
                            }

                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public static void loadWishList(final Context context, final Dialog dialog, final boolean loadProductData){
        wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            for (long x = 0; x < (long) task.getResult().get("list_size"); x++){
                                wishList.add(task.getResult().get("product_ID_"+x).toString());

                                if (DBqueries.wishList.contains(ProductDetailsActivity.productID)){
                                    ProductDetailsActivity. ALREADY_ADDED_TO_WISHLIST = true;
                                    if (ProductDetailsActivity.addToWishlistBtn != null) {
                                        ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorRed));
                                    }
                                }else {
                                    if (ProductDetailsActivity.addToWishlistBtn != null) {
                                        ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                    }
                                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                                }

                                if (loadProductData) {
                                    wishlistModelList.clear();
                                    String productID = task.getResult().get("product_ID_" + x).toString();
                                    firebaseFirestore.collection("PRODUCTS").document(productID)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()){

                                                                            if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")){

                                                                                wishlistModelList.add(new WishlistModel(productID,documentSnapshot.get("product_image_1").toString()
                                                                                        , documentSnapshot.get("product_title").toString()
                                                                                        , (long)documentSnapshot.get("free_coupens")
                                                                                        , documentSnapshot.get("average_rating").toString()
                                                                                        , (long) documentSnapshot.get("total_ratings")
                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                        , (boolean) documentSnapshot.get("COD")
                                                                                        ,true));
                                                                            }else {
                                                                                wishlistModelList.add(new WishlistModel(productID,documentSnapshot.get("product_image_1").toString()
                                                                                        , documentSnapshot.get("product_title").toString()
                                                                                        , (long)documentSnapshot.get("free_coupens")
                                                                                        , documentSnapshot.get("average_rating").toString()
                                                                                        , (long) documentSnapshot.get("total_ratings")
                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                        , (boolean) documentSnapshot.get("COD")
                                                                                        ,false));
                                                                            }
                                                                            MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();

                                                                        }else {
                                                                            //error
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                });


                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                }
                            }
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    public static void removeFromWishlist(int index, Context context) {
        String removedProductId = wishList.get(index);
        wishList.remove(index);
        Map<String, Object> updateWishlist = new HashMap<>();

        for (int x = 0; x < wishList.size(); x++) {
            updateWishlist.put("product_ID_" + x, wishList.get(x));
        }
        updateWishlist.put("list_size", (long) wishList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (wishlistModelList.size() != 0){
                                wishlistModelList.remove(index);
                                MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                            }
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                            Toast.makeText(context, "Removed successfully!", Toast.LENGTH_SHORT).show();

                        }else{
                            if (ProductDetailsActivity.addToWishlistBtn != null) {
                                ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorRed));
                            }
                            wishList.add(index,removedProductId);
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }

                        ProductDetailsActivity.running_widhlist_query = false;
//                        ProductDetailsActivity.addToWishlistBtn.setEnabled(true);

                    }
                });
    }

    public static void loadRatingList(Context context){
        if (!ProductDetailsActivity.running_rating_query) {
            ProductDetailsActivity.running_rating_query = true;
            myRatedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                List<String> orderProductIds = new ArrayList<>();
                                for (int x = 0; x < myOrderItemModelList.size(); x++) {
                                    orderProductIds.add(myOrderItemModelList.get(x).getProductId());
                                }


                                for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                                    myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                                    myRating.add((long) task.getResult().get("rating_" + x));

                                    if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailsActivity.productID)) {
                                        ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                        if (ProductDetailsActivity.rateNowCantainer != null) {
                                            ProductDetailsActivity.setReting(ProductDetailsActivity.initialRating);
                                        }
                                    }

                                    if (orderProductIds.contains(task.getResult().get("product_ID_" + x).toString())) {
                                        myOrderItemModelList.get(orderProductIds.indexOf(task.getResult().get("product_ID_" + x).toString())).setRating(Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1);
                                    }

                                }
                                if (MyOrdersFragment.myOrderAdapter != null) {
                                    MyOrdersFragment.myOrderAdapter.notifyDataSetChanged();
                                }

                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                            }
                            ProductDetailsActivity.running_rating_query = false;
                        }
                    });
        }
    }

    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount,final TextView cartTotalAmount){
        cartList.clear();
        cartItemModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                                cartList.add(task.getResult().get("product_ID_" + x).toString());

                                if (DBqueries.cartList.contains(ProductDetailsActivity.productID)) {
                                    ProductDetailsActivity.ALREADY_ADDED_TO_CART = true;
                                } else {
                                    ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;
                                }

                                if (loadProductData) {
                                    cartItemModelList.clear();
                                    String productID = task.getResult().get("product_ID_" + x).toString();
                                    firebaseFirestore.collection("PRODUCTS").document(productID)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {

                                                                            int index = 0;
                                                                            if (cartList.size() >= 2) {
                                                                                index = cartList.size() - 2;
                                                                            }

                                                                            if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {

                                                                                cartItemModelList.add(index, new CartItemModel(documentSnapshot.getBoolean("COD"),CartItemModel.CART_ITEM, productID
                                                                                        , documentSnapshot.get("product_image_1").toString()
                                                                                        , documentSnapshot.get("product_title").toString()
                                                                                        , (long) documentSnapshot.get("free_coupens")
                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                        , (long) 1
                                                                                        , (long) documentSnapshot.get("offers_applied")
                                                                                        , (long) 0
                                                                                        , true
                                                                                        , (long) documentSnapshot.get("max_quantity")
                                                                                        , (long) documentSnapshot.get("stock_quantity")));
                                                                            } else {
                                                                                cartItemModelList.add(index, new CartItemModel(documentSnapshot.getBoolean("COD"),CartItemModel.CART_ITEM, productID
                                                                                        , documentSnapshot.get("product_image_1").toString()
                                                                                        , documentSnapshot.get("product_title").toString()
                                                                                        , (long) documentSnapshot.get("free_coupens")
                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                        , (long) 1
                                                                                        , (long) documentSnapshot.get("offers_applied")
                                                                                        , (long) 0
                                                                                        , false
                                                                                        , (long) documentSnapshot.get("max_quantity")
                                                                                        , (long) documentSnapshot.get("stock_quantity")));
                                                                            }
                                                                            if (cartList.size() == 1) {
                                                                                cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                                                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                                                parent.setVisibility(View.VISIBLE);
                                                                            }
                                                                            MyCartFragment.cartAdapter.notifyDataSetChanged();

                                                                        } else {
                                                                            //error
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                });


                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                }
                            }


/// Update badge count
                            if (badgeCount != null) {
                                if (cartList.size() > 0) {
                                    badgeCount.setVisibility(View.VISIBLE);
                                    badgeCount.setText(String.valueOf(cartList.size()));
                                } else {
                                    badgeCount.setVisibility(View.GONE);
                                }
                            } else {
                            cartList.clear();
                            cartItemModelList.clear();
                        }
                        dialog.dismiss();
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
         });
    }


    public static void removeFromCart(int index, Context context,TextView cartTotalAmount) {
        String removedProductId = cartList.get(index);
        cartList.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();

        for (int x = 0; x < cartList.size(); x++) {
            updateCartList.put("product_ID_" + x, cartList.get(x));
        }
        updateCartList.put("list_size", (long) cartList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (cartItemModelList.size() != 0){
                                cartItemModelList.remove(index);
                                MyCartFragment.cartAdapter.notifyDataSetChanged();
                            }
                            if (cartList.size() == 0) {
                                cartItemModelList.clear();
                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                parent.setVisibility(View.GONE);
                            }else {
                                MyCartFragment.cartAdapter.notifyDataSetChanged();
                            }
                            Toast.makeText(context, "Removed successfully!", Toast.LENGTH_SHORT).show();

                        }else{
                            cartList.add(index,removedProductId);
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }

                        ProductDetailsActivity.running_cart_query = false;

                    }
                });
    }

    public static void loadAddresses(final Context context,final Dialog loadingDialog,boolean gotoDeliveryActivity){
        addressesModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Intent deliverIntent = null;
                            if ((long) task.getResult().get("list_size") == 0){
                                 deliverIntent = new Intent(context, AddAddressActivity.class);
                                 deliverIntent.putExtra("INTENT","deliverIntent");
                            }else {
                                for (long x = 1; x < (long) task.getResult().get("list_size") + 1; x++) {
                                    addressesModelList.add(new AddressesModel(task.getResult().getBoolean("selected_" + x)
                                    ,task.getResult().getString("city_" + x)
                                    ,task.getResult().getString("locality_" + x)
                                    ,task.getResult().getString("flat_no_" + x)
                                    ,task.getResult().getString("pincode_" + x)
                                    ,task.getResult().getString("landmark_" + x)
                                    ,task.getResult().getString("name_" + x)
                                    ,task.getResult().getString("mobile_no_" + x)
                                    ,task.getResult().getString("alternate_mobile_no_" + x)
                                            ,task.getResult().getString("state_" + x)
                                    ));
                                    if ((boolean) task.getResult().get("selected_"+x)){
                                        selectedAddress = Integer.parseInt(String.valueOf(x-1));
                                    }
                                }
                                if (gotoDeliveryActivity) {
                                    deliverIntent = new Intent(context, DeliveryActivity.class);
                                }

                            }
                            if (gotoDeliveryActivity) {
                                context.startActivity(deliverIntent);
                            }
                         }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }

    public static void loadRewards(Context context, final Dialog loadingDialog,Boolean onRewardFragment) {
        rewardModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {

                                    Date lastseenDate = task.getResult().getDate("Last_seen"); // Date lastseenDate = task.getResult().getTimestamp("Last_seen").toDate(); isko dalna h agar ish date pr error ayi to

                                    firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                            if (documentSnapshot.get("type").toString().equals("Discount") && lastseenDate.before(documentSnapshot.getTimestamp("validity").toDate())) {
                                                                rewardModelList.add(new RewardModel(documentSnapshot.getId(),
                                                                        documentSnapshot.get("type").toString(),
                                                                        documentSnapshot.get("lower_limit").toString(),
                                                                        documentSnapshot.get("upper_limit").toString(),
                                                                        documentSnapshot.get("percentage").toString(),
                                                                        documentSnapshot.get("body").toString(),
                                                                        documentSnapshot.getTimestamp("validity").toDate(),
                                                                        (boolean)documentSnapshot.get("already_used")
                                                                ));
                                                            } else if (documentSnapshot.get("type").toString().trim().contains("Flat Rs.* OFF") && lastseenDate.before(documentSnapshot.getTimestamp("validity").toDate())){
                                                                rewardModelList.add(new RewardModel(documentSnapshot.getId(),
                                                                        documentSnapshot.get("type").toString(),
                                                                        documentSnapshot.get("lower_limit").toString(),
                                                                        documentSnapshot.get("upper_limit").toString(),
                                                                        documentSnapshot.get("amount").toString(),
                                                                        documentSnapshot.get("body").toString(),
                                                                        documentSnapshot.getTimestamp("validity").toDate(),
                                                                        (boolean)documentSnapshot.get("already_used")
                                                                ));
                                                            }
                                                        }
                                                        if (onRewardFragment) {
                                                            MyRewardsFragment.myRewardsAdapter.notifyDataSetChanged();
                                                        }

                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    loadingDialog.dismiss();
                                                }
                                            });
                                    }else {
                                    loadingDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                }
                                }
                        });




    }

    public static void loadOrders(Context context, @Nullable MyOrderAdapter myOrderAdapter, final Dialog loadingDialog){
        myOrderItemModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").orderBy("time", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                firebaseFirestore.collection("ORDERS").document(documentSnapshot.getString("ORDER_ID")).collection("OrderItems")
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (DocumentSnapshot orderItems : task.getResult().getDocuments()) {

                                                        MyOrderItemModel myOrderItemModel = new MyOrderItemModel(orderItems.getString("Product_Id"), orderItems.getString("Order_Status"), orderItems.getString("Address"), orderItems.getString("Coupen_Id"), orderItems.getString("Cutted_Price"), orderItems.getDate("Ordered date"), orderItems.getDate("Packed date"), orderItems.getDate("Shipped date"), orderItems.getDate("Deliverd date"), orderItems.getDate("Cancelled date"), orderItems.getString("Discounted_Price"), orderItems.getLong("Free_Coupen"), orderItems.getString("FullName"), orderItems.getString("ORDER_ID"), orderItems.getString("Payment_Method"), orderItems.getString("Pinecode"), orderItems.getString("Product_Price"), orderItems.getLong("Product_Quantity"), orderItems.getString("User_Id"),orderItems.getString("Product_Image"),orderItems.getString("Product_Title"),orderItems.getString("Delivery Price"),orderItems.getBoolean("Cancellation requested"));
                                                        myOrderItemModelList.add(myOrderItemModel);
                                                    }
                                                        loadRatingList(context);
                                                    if (myOrderAdapter != null) {
                                                        myOrderAdapter.notifyDataSetChanged();
                                                    }

                                                }else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });
                            }
                        }else {
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void checkNotifications(boolean remove,@Nullable TextView notifyCount) {

        if (remove){
            registration.remove();

        }else {
            registration = firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_NOTIFICATIONS")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                notificationModelList.clear();
                                int unread = 0;
                                for (long x = 0; x < (long) documentSnapshot.get("list_size"); x++) {
                                    notificationModelList.add(new NotificationModel(documentSnapshot.get("Image_" + x).toString(), documentSnapshot.get("Body_" + x).toString(), documentSnapshot.getBoolean("Readed_" + x)));
                                    if (!documentSnapshot.getBoolean("Readed_" + x)) {
                                        unread++;
                                        if (notifyCount != null) {
                                            if (unread > 0) {
                                                notifyCount.setVisibility(View.VISIBLE);
                                                if (unread < 99) {
                                                    notifyCount.setText(String.valueOf(unread));
                                                } else {
                                                    notifyCount.setText("99");
                                                }
                                            } else {
                                                notifyCount.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    }
                                }
                                if (NotificationActivity.adapter != null) {
                                    NotificationActivity.adapter.notifyDataSetChanged();
                                }

                            }
                        }
                    });
        }




    }

    public static void clearData () {
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        wishList.clear();
        wishlistModelList.clear();
        cartList.clear();
        cartItemModelList.clear();
        myRatedIds.clear();
        myRating.clear();
        addressesModelList.clear();
        rewardModelList.clear();
        myOrderItemModelList.clear();
    }

}
