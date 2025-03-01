package com.example.ecommerce;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.Context;

import java.util.List;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {
    private List<ProductSpecificationModel> productSpecificationModelList;
    private android.view.ViewGroup ViewGroup;

    public ProductSpecificationAdapter(List<ProductSpecificationModel> productSpecificationModelList) {
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModelList.get(position).getType()) {
            case 0:
                return ProductSpecificationModel.SPECIFICATION_TITLE;
            case 1:
                return ProductSpecificationModel.SPECIFICATION_BODY;
            default:
                return -1;

        }
    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                TextView title = new TextView(ViewGroup.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16, ViewGroup.getContext())
                        , setDp(16, ViewGroup.getContext()),
                        setDp(16, ViewGroup.getContext()),
                        setDp(8, ViewGroup.getContext()));
                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);

            case ProductSpecificationModel.SPECIFICATION_BODY:
                View view = LayoutInflater.from(ViewGroup.getContext()).inflate(R.layout.product_specification_item_layout, ViewGroup, false);
                return new ViewHolder(view);
            default:
                return null;

        }

    }



    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder holder, int position) {

        switch (productSpecificationModelList.get(position).getType()){
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                ViewHolder.setTitle(productSpecificationModelList.get(position).getTitle());
                break;
            case ProductSpecificationModel.SPECIFICATION_BODY:
                String featureTitle = productSpecificationModelList.get(position).getFeatureName();
                String featureDetail = productSpecificationModelList.get(position).getFeatureValue();
                ViewHolder.setFeature(featureTitle, featureDetail);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return productSpecificationModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static TextView featureName;
        private static TextView featureValue;
        private static TextView title;
        private static TextView itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        private static void setTitle(String titleText){
            title = (TextView) itemView;
            title.setText(titleText);
        }
        private static void setFeature(String featureTitle, String featuredetail) {
            featureName = itemView.findViewById(R.id.feature_name);
            featureValue = itemView.findViewById(R.id.feature_value);
            featureName.setText(featureTitle);
            featureValue.setText(featuredetail);
        }
    }

    private int setDp(int dp, android.content.Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
