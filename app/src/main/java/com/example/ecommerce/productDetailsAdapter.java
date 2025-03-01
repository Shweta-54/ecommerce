package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class productDetailsAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    public productDetailsAdapter(@NonNull FragmentManager fm,int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                ProductDescriptionFragment productDescriptionFragmentnt1 = new ProductDescriptionFragment();
                return productDescriptionFragmentnt1;
                case 1:
                    ProductSpecificationFragment productSpecificationFragment = new ProductSpecificationFragment();
                    return productSpecificationFragment;
            case 2:
                ProductDescriptionFragment productDescriptionFragmentnt2 = new ProductDescriptionFragment();
                return productDescriptionFragmentnt2;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
