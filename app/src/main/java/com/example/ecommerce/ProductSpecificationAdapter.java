package com.example.ecommerce;

import android.content.Context;
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

import java.util.List;

/** @noinspection ALL*/
public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {

    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductSpecificationAdapter(List<ProductSpecificationModel> productSpecificationModelList) {
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @Override
    public int getItemViewType(int position) {
        return switch (productSpecificationModelList.get(position).getType()) {
            case 0 -> ProductSpecificationModel.SPECIFICATION_TITLE;
            case 1 -> ProductSpecificationModel.SPECIFICATION_BODY;
            default -> -1;
        };
    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                TextView title = new TextView(viewGroup.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16, viewGroup.getContext())
                        , setDp(16, viewGroup.getContext()),
                        setDp(16, viewGroup.getContext()),
                        setDp(8, viewGroup.getContext()));
                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);

            case ProductSpecificationModel.SPECIFICATION_BODY:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_specification_item_layout, viewGroup, false);
                return new ViewHolder(view);
            default:
                return null;

        }

    }



    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder viewHolder, int position) {

        switch (productSpecificationModelList.get(position).getType()){
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                viewHolder.setTitle(productSpecificationModelList.get(position).getTitle());
                break;
            case ProductSpecificationModel.SPECIFICATION_BODY:
                String featureTitle = productSpecificationModelList.get(position).getFeatureName();
                String featureDetail = productSpecificationModelList.get(position).getFeatureValue();
                viewHolder.setFeature(featureTitle, featureDetail);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return (productSpecificationModelList != null) ? productSpecificationModelList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private  TextView featureName;
        private  TextView featureValue;
        private  TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Check if itemView is a TextView (for title case)
            if (itemView instanceof TextView) {
                title = (TextView) itemView;
            } else {
                // Otherwise, initialize featureName and featureValue
                featureName = itemView.findViewById(R.id.feature_name);
                featureValue = itemView.findViewById(R.id.feature_value);
            }
        }
        private  void setTitle(String titleText){
            if (title != null) {
                title.setText(titleText);
            }
        }
        private  void setFeature(String featureTitle, String featuredetail) {
            if (featureName != null && featureValue != null) {
                featureName.setText(featureTitle);
                featureValue.setText(featuredetail);
            }
        }
    }

    private int setDp(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}