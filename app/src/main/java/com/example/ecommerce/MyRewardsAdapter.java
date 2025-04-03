package com.example.ecommerce;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {
    private final List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;
    private RecyclerView coupensRecyclerView;
    private LinearLayout selectedCoupen;
    private String productOriginalPrice;
    private TextView selectedcoupentitle;
    private TextView selectedcoupenExpiryDate;
    private TextView selectedcoupenBody;
    private TextView discountedPrice;
    private int cartItemPosition = -1;
    private List<CartItemModel> cartItemModelList;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }

    public MyRewardsAdapter(int cartItemPosition,List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupentitle, TextView coupenExpiryDate, TextView coupenBody, TextView discountedPrice, List<CartItemModel> cartItemModelList) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedcoupenBody = coupenBody;
        this.selectedcoupenExpiryDate = coupenExpiryDate;
        this.selectedcoupentitle = coupentitle;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.discountedPrice = discountedPrice;
        this.cartItemPosition = cartItemPosition;
        this.cartItemModelList = cartItemModelList;
    }

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupentitle, TextView coupenExpiryDate, TextView coupenBody, TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedcoupenBody = coupenBody;
        this.selectedcoupenExpiryDate = coupenExpiryDate;
        this.selectedcoupentitle = coupentitle;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.discountedPrice = discountedPrice;
    }



    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (useMiniLayout) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);
        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        String coupenId = rewardModelList.get(position).getCoupenId();
        String type = rewardModelList.get(position).getType();
        Date validity = rewardModelList.get(position).getTimestamp();
        String body = rewardModelList.get(position).getCoupenBody();
        String lowerLimit = rewardModelList.get(position).getLowerLimit();
        String upperLimit = rewardModelList.get(position).getUpperLimit();
        String discORamt = rewardModelList.get(position).getdiscORamt();
        Boolean alreadyUsed = rewardModelList.get(position).getAlreadyUsed();
        viewholder.setData(coupenId,type, validity, body, upperLimit, lowerLimit, discORamt,alreadyUsed);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final TextView coupenTitle;
        private final TextView coupenExpriryDate;
        private final TextView coupenBody;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            coupenTitle = itemView.findViewById(R.id.coupen_title);
            coupenExpriryDate = itemView.findViewById(R.id.coupen_validity);
            coupenBody = itemView.findViewById(R.id.coupen_body);
        }

        private void setData(String coupenId,String type, Date validity, String body, String upperLimit, String lowerLimit, String discORamt,boolean alreadyUsed) {
            if (type.equals("Discount")) {
                coupenTitle.setText(type);
            } else {
                coupenTitle.setText("FLAT Rs." + discORamt + "/- OFF");
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");

            if (alreadyUsed) {
                coupenExpriryDate.setText("Already Used");
                coupenExpriryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.lavender));
                coupenBody.setTextColor(Color.parseColor("#50ffffff"));
                coupenTitle.setTextColor(Color.parseColor("#50ffffff"));
            }else {
                coupenBody.setTextColor(Color.parseColor("#ffffff"));
                coupenTitle.setTextColor(Color.parseColor("#ffffff"));
                coupenExpriryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.coupenPurple));
                coupenExpriryDate.setText("Till " + simpleDateFormat.format(validity));
            }
            coupenBody.setText(body);

            if (useMiniLayout) {
                itemView.setOnClickListener(v -> {

                    if (!alreadyUsed) {
                        selectedcoupentitle.setText(type);
                        selectedcoupenExpiryDate.setText(simpleDateFormat.format(validity));
                        selectedcoupenBody.setText(body);


                        if (Long.valueOf(productOriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upperLimit)) {
                            if (type.equals("Discount")) {
                                Long discountAmount = Long.valueOf(productOriginalPrice) * Long.valueOf(discORamt) / 100;
                                discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount) + "/-");
                            } else {
                                discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(discORamt)) + "/-");
                            }
                            if (cartItemPosition != -1) {
                               cartItemModelList.get(cartItemPosition).setSelectedCoupenId(coupenId);
                            }
                        } else {
                            if (cartItemPosition != -1) {
                               cartItemModelList.get(cartItemPosition).setSelectedCoupenId(null);
                            }
                            discountedPrice.setText("N/A");
                            Toast.makeText(itemView.getContext(), "Sorry, this coupen is not applicable on this term", Toast.LENGTH_SHORT).show();
                        }
                        if (coupensRecyclerView.getVisibility() == View.GONE) {
                            coupensRecyclerView.setVisibility(View.VISIBLE);
                            selectedCoupen.setVisibility(View.GONE);
                        } else {
                            coupensRecyclerView.setVisibility(View.GONE);
                            selectedCoupen.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }
    }
}
