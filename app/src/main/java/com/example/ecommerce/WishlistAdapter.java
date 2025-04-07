package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {


    private boolean fromSearch;
    private List<WishlistModel> wishlistModelList;
    private final Boolean wishlist;
    private int lastPosition = -1;

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public WishlistAdapter(List<WishlistModel> wishlistModelList,Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }

    public List<WishlistModel> getWishlistModelList() {
        return wishlistModelList;
    }
    public void setWishlistModelList(List<WishlistModel> wishlistModelList){
        this.wishlistModelList = wishlistModelList;
    }


    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        String productID = wishlistModelList.get(position).getProductId();
        String resource = wishlistModelList.get(position).getProductImage();
        String title = wishlistModelList.get(position).getProductTitle();
        long freeCoupens = wishlistModelList.get(position).getFreeCoupens();
        String rating = wishlistModelList.get(position).getRating();
        long totalRatings = wishlistModelList.get(position).getTotalRatings();
        String productPrice = wishlistModelList.get(position).getProductPrice();
        String cuttedPrice = wishlistModelList.get(position).getCuttedPrice();
        boolean paymentMethod = wishlistModelList.get(position).isCOD();
        boolean inStock = wishlistModelList.get(position).isInStock();
        viewHolder.setData(productID,resource,title,freeCoupens,rating,totalRatings,productPrice,cuttedPrice,paymentMethod,position,inStock);

        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView productImage;
        private final TextView productTitle;
        private final TextView freecoupens;
        private final ImageView coupenIcon;
        private final TextView rating;
        private final TextView totalRatings;
        private final View priceCut;
        private final TextView productPrice;
        private final TextView cuttedPrice;
        private final TextView paymentMethod;
        private final ImageButton deleteBtn;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image1);
            productTitle = itemView.findViewById(R.id.product_title1);
            freecoupens = itemView.findViewById(R.id.free_coupens);
            coupenIcon = itemView.findViewById(R.id.coupen_icons);
            rating = itemView.findViewById(R.id.tv_product_rating_miniview1);
            totalRatings = itemView.findViewById(R.id.total_ratings1);
            priceCut = itemView.findViewById(R.id.price_cut);
            productPrice = itemView.findViewById(R.id.product_price1);
            cuttedPrice = itemView.findViewById(R.id.cutted_pricess);
            paymentMethod = itemView.findViewById(R.id.payment_method);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
        }

        @SuppressLint("SetTextI18n")
        private void  setData(String productID,String resource, String title, long freecoupensNo, String averageRate, long totalRatingsNo, String price, String cuttedPriceValue, boolean COD,int index,boolean inStock) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.paceholder)).into(productImage);
            productTitle.setText(title);
            if (freecoupensNo != 0 && inStock) {
                coupenIcon.setVisibility(View.VISIBLE);
                if (freecoupensNo == 1) {
                    freecoupens.setText("free " + freecoupensNo + " coupen");
                } else {
                    freecoupens.setText("free " + freecoupensNo + " coupen");
                }
            }else {
                coupenIcon.setVisibility(View.INVISIBLE);
                freecoupens.setVisibility(View.INVISIBLE);
            }
            LinearLayout linearLayout = (LinearLayout) rating.getParent();
            if (inStock) {
                rating.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                cuttedPrice.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);

                rating.setText(averageRate);
                totalRatings.setText("("+totalRatingsNo+")ratings");
                productPrice.setText("Rs."+price+"/-");
                cuttedPrice.setText("Rs."+cuttedPriceValue+"/-");
                if (COD){
                    paymentMethod.setVisibility(View.VISIBLE);
                }else{
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            }else {

                linearLayout.setVisibility(View.INVISIBLE);
                rating.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                productPrice.setText("Out of stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                cuttedPrice.setVisibility(View.INVISIBLE);
                paymentMethod.setVisibility(View.INVISIBLE);

            }


            if (wishlist){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(v -> {
                if (!ProductDetailsActivity.running_widhlist_query) {
                    ProductDetailsActivity.running_widhlist_query = true;
                    DBqueries.removeFromWishlist(index, itemView.getContext());
                }
            });
            itemView.setOnClickListener(view -> {
                Intent productDetailsIntent = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                productDetailsIntent.putExtra("PRODUCT_ID",productID);
                itemView.getContext().startActivity(productDetailsIntent);
            });
        }
    }
}