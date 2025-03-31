package com.example.ecommerce;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {
    private final List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
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
        String type = rewardModelList.get(position).getType();
        Date validity = rewardModelList.get(position).getTimestamp();
        String body = rewardModelList.get(position).getCoupenBody();
        String lowerLimit = rewardModelList.get(position).getLowerLimit();
        String upperLimit = rewardModelList.get(position).getUpperLimit();
        String discORamt = rewardModelList.get(position).getdiscORamt();
        viewholder.setData(type, validity, body, upperLimit, lowerLimit, discORamt);
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

        private void setData(String type, Date validity, String body, String upperLimit, String lowerLimit, String discORamt) {
            if (type.equals("Discount")) {
                coupenTitle.setText(type);
            } else {
                coupenTitle.setText("FLAT Rs." + discORamt + "/- OFF");
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            if (validity != null) {
                coupenExpriryDate.setText("Till " + simpleDateFormat.format(validity));
            } else {
                coupenExpriryDate.setText("Validity not available");
            }

            coupenBody.setText(body);
            if (useMiniLayout) {
                itemView.setOnClickListener(v -> {
                    ProductDetailsActivity.coupentitle.setText(type);
                    ProductDetailsActivity.coupenExpiryDate.setText(simpleDateFormat.format(validity));
                    ProductDetailsActivity.coupenBody.setText(body);
                    ProductDetailsActivity.showDialogRecyclerView();
                });
            }
        }
    }
}
