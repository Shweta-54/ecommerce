package com.example.ecommerce;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {
    private final List<RewardModel> rewardModelList;
    private  Boolean useMiniLayout = false;

    public MyRewardsAdapter(List<RewardModel> rewardModelList,Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (useMiniLayout){
             view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);

        }else {
             view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);

        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        String title = rewardModelList.get(position).getTitle();
        String date = rewardModelList.get(position).getExpiryDate();
        String body = rewardModelList.get(position).getCoupenBody();
        viewholder.setData(title,date,body);

    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private final TextView coupenTitle;
        private final TextView coupenExpriryDate;
        private final TextView coupenBody;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            coupenTitle = itemView.findViewById(R.id.coupen_title);
            coupenExpriryDate = itemView.findViewById(R.id.coupen_validity);
            coupenBody = itemView.findViewById(R.id.coupen_body);
        }
        private void setData(String title,String date,String body){
            coupenTitle.setText(title);
            coupenExpriryDate.setText(date);
            coupenBody.setText(body);


            if (useMiniLayout){
                itemView.setOnClickListener(v -> {

                    ProductDetailsActivity.coupentitle.setText(title);
                    ProductDetailsActivity.coupenExpiryDate.setText(date);
                    ProductDetailsActivity.coupenBody.setText(body);
                    ProductDetailsActivity.showDialogRecyclerView();
                });
            }
        }

    }
}
