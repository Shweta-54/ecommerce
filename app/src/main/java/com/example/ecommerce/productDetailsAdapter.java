package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class productDetailsAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    private String productDescription;
    private String productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList;

    public productDetailsAdapter(@NonNull FragmentManager fm, int totalTabs,String productDescription, String productOtherDetails, List<ProductSpecificationModel> productSpecificationModelList) {
        super(fm);
        this.productDescription = productDescription;
        this.productOtherDetails = productOtherDetails;
        this.productSpecificationModelList = productSpecificationModelList;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                ProductDescriptionFragment productDescriptionFragmentnt1 = new ProductDescriptionFragment();
                productDescriptionFragmentnt1.body = productDescription;
                return productDescriptionFragmentnt1;
                case 1:
                    ProductSpecificationFragment productSpecificationFragment = new ProductSpecificationFragment();
                    productSpecificationFragment.productSpecificationModelList = productSpecificationModelList;
                    return productSpecificationFragment;
            case 2:
                ProductDescriptionFragment productDescriptionFragmentnt2 = new ProductDescriptionFragment();
                productDescriptionFragmentnt2.body = productDescription;
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
