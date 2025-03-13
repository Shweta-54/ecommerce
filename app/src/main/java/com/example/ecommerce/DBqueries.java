package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** @noinspection ALL*/
public class DBqueries {

    @SuppressLint("StaticFieldLeak")
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public   static List<CategoryModel> categoryModelList = new ArrayList<>();
    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public static void loadCategories(final CategoryAdapter categoryAdapter, Context context){

        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            categoryModelList.add(new CategoryModel(Objects.requireNonNull(documentSnapshot.get("icon")).toString(), Objects.requireNonNull(documentSnapshot.get("categoryName")).toString()));
                        }
                        categoryAdapter.notifyDataSetChanged();

                    }else{
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void  loadFragmentData( final HomePageAdapter adapter,Context context,final int index,String categoryName){
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase()).collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                            if ((long)documentSnapshot.get("view_type") == 0){
                                List<SliderModel> sliderModelList = new ArrayList<>();
                                long no_of_banners = (long)documentSnapshot.get("no_of_banners");
                                for (long x = 1;x < no_of_banners + 1;x++){
                                    sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+x).toString()
                                            ,documentSnapshot.get("banner_"+x+"_background").toString()));
                                }
                                lists.get(index).add(new HomePageModel(0,sliderModelList));
                            }else if ((long)documentSnapshot.get("view_type") == 1){
                                lists.get(index).add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString()
                                        ,documentSnapshot.get("background").toString()));
                            } else if ((long)documentSnapshot.get("view_type") == 2) {

                                List<WishlistModel> viewAllProductList = new ArrayList<>();
                                List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                long no_of_products = (long)documentSnapshot.get("no_of_products");
                                for (long x = 1;x < no_of_products + 1;x++){
                                    horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_"+x).toString()
                                            ,documentSnapshot.get("product_image_"+x).toString()
                                            ,documentSnapshot.get("product_title_"+x).toString()
                                            ,documentSnapshot.get("product_subtitle_"+x).toString()
                                            ,documentSnapshot.get("product_price_"+x).toString()));

                                    viewAllProductList.add(new WishlistModel(documentSnapshot.get("product_image_"+x).toString()
                                            ,documentSnapshot.get("product_full_title_"+x).toString()
                                            ,(long)documentSnapshot.get("free_coupens_"+x)
                                            ,documentSnapshot.get("average_rating_"+x).toString()
                                            ,(long)documentSnapshot.get("total_rating_"+x)
                                            ,documentSnapshot.get("product_price_"+x).toString()
                                            ,documentSnapshot.get("cutted_price_"+x).toString()
                                            ,(boolean) documentSnapshot.get("COD_"+x)));
                                }
                                lists.get(index).add(new HomePageModel(2,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),horizontalProductScrollModelList,viewAllProductList));
                            } else if ((long)documentSnapshot.get("view_type") == 3) {
                                List<HorizontalProductScrollModel> GridLayoutModelList = new ArrayList<>();
                                long no_of_products = (long)documentSnapshot.get("no_of_products");
                                for (long x = 1;x < no_of_products + 1;x++){
                                    GridLayoutModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_"+x).toString()
                                            ,documentSnapshot.get("product_image_"+x).toString()
                                            ,documentSnapshot.get("product_title_"+x).toString()
                                            ,documentSnapshot.get("product_subtitle_"+x).toString()
                                            ,documentSnapshot.get("product_price_"+x).toString()));
                                }
                                lists.get(index).add(new HomePageModel(3,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),GridLayoutModelList));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}