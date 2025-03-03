package com.example.ecommerce;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyOrederAdapter extends RecyclerView.Adapter<MyOrederAdapter.viewholder> {

    private List<MyOrderItemModel> myOrderItemModelList;

    public MyOrederAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }




    @NonNull
    @Override
    public MyOrederAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_oder_item_layout,viewGroup,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrederAdapter.viewholder viewholder, int i) {

    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    class viewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView deliveryStatus;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image3);
            productTitle = itemView.findViewById(R.id.product_title3);
            orderIndicator = itemView.findViewById(R.id.order_indicator);
            deliveryStatus = itemView.findViewById(R.id.order_delivered_date);
        }
        private  void setData(int resource,String title,String deliveryDate){
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if (deliveryDate.equals("Cancelled")){
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorRed)));
            }else {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.SuccessGreen)));
            }
                deliveryStatus.setText(deliveryDate);


        }
    }
}