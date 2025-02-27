package com.example.ecommerce;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ProductImagesAdapter extends PagerAdapter {

    private List<Integer> productImages;

    public ProductImagesAdapter(List<Integer> productImages) {

        this.productImages = productImages;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView productImage = new ImageView(container.getContext());
        // ✅ List<Integer> use karo na ki ImageView
        productImage.setImageResource(productImages.get(position));  // ✅ ERROR FIXED

        container.addView(productImage, 0);
        return productImage;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

    @Override
    public int getCount() {

        return productImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
