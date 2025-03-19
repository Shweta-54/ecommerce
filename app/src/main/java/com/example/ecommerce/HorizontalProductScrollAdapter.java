package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.viewHolder> {

    private final List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item_layout,viewGroup,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.viewHolder viewHolder, int position) {
         String resource = horizontalProductScrollModelList.get(position).getProductImage();
         String title = horizontalProductScrollModelList.get(position).getProductTitle();
         String description = horizontalProductScrollModelList.get(position).getProductDescription();
         String price = horizontalProductScrollModelList.get(position).getProductPrice();
         String productId = horizontalProductScrollModelList.get(position).getProductID();

         viewHolder.setData(productId,resource,title,description,price);


    }

    @Override
    public int getItemCount() {
        return Math.min(horizontalProductScrollModelList.size(), 8);
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private final ImageView productImage;
        private final TextView productTitle;
        private final TextView productDescription;
        private final TextView productPrice;

        public viewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.h_s_product_image);
            productTitle = itemView.findViewById(R.id.h_s_product_title);
            productDescription = itemView.findViewById(R.id.h_s_product_describtion);
            productPrice = itemView.findViewById(R.id.h_s_product_price);



        }

        private void setData(String productId,String resource,String title,String description,String price) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.paceholder)).into(productImage);
            productPrice.setText("Rs." + price + "/-");
            productDescription.setText(description);
            productTitle.setText(title);

            if (!title.equals("")) {
                itemView.setOnClickListener(view -> {
                    Intent productDetailIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productDetailIntent.putExtra("PRODUCT_ID",productId);
                    itemView.getContext().startActivity(productDetailIntent);
                });
            }
        }


    }
}
