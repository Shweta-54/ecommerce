package com.example.ecommerce;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class OrderDetailsActivity extends AppCompatActivity {

    private int position;
    private TextView title,price,quantity;
    private ImageView productImage,orderIndicator,packegeIndicator,shippingIndicator,deliveryIndicator;
    private ProgressBar O_P_progress,P_S_progress,S_D_progress;
    private TextView orderedTitle,packedTitle,shippingTitle,deliveredTitle;
    private TextView orderedDate,packedDate,shippingDate,deliveredDate;
    private TextView orderedBody,packedBody,shippingBody,deliveredBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);
        // ✅ Initialize View Binding
//        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        // ✅ Initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        position = getIntent().getIntExtra("Position",-1);
        MyOrderItemModel model = DBqueries.myOrderItemModelList.get(position);
        title = findViewById(R.id.product_title4);
        price = findViewById(R.id.product_price4);
        quantity = findViewById(R.id.product_quantity4);
        productImage = findViewById(R.id.product_image4);
        orderIndicator = findViewById(R.id.order_indicator1);
        packegeIndicator = findViewById(R.id.packed_indicator);
        shippingIndicator = findViewById(R.id.shipping_indicator);
        deliveryIndicator = findViewById(R.id.delivered_indicator);
        O_P_progress = findViewById(R.id.ordered_packed_progress);
        P_S_progress = findViewById(R.id.packed_shipping_progress);
        S_D_progress = findViewById(R.id.shipping_delivered);
        orderedTitle = findViewById(R.id.ordered_title);
        packedTitle = findViewById(R.id.packed_title);
        shippingTitle = findViewById(R.id.shipping_title);
        deliveredTitle = findViewById(R.id.deliverd_title);
        orderedDate = findViewById(R.id.ordered_date);
        packedDate = findViewById(R.id.packed_date);
        shippingDate = findViewById(R.id.shipping_date);
        deliveredDate = findViewById(R.id.deliverd_date);
        orderedBody = findViewById(R.id.ordered_body);
        packedBody = findViewById(R.id.packed_body);
        shippingBody = findViewById(R.id.shipping_body);
        deliveredBody = findViewById(R.id.deliverd_body);

        title.setText(model.getProductTitle());
        if (model.getDiscountedPrice() != null) {
            price.setText(model.getDiscountedPrice());
        }else {
         price.setText(model.getProductPrice());
        }
        quantity.setText(String.valueOf(model.getProductQuantity()));
        Glide.with(this).load(model.getProductImage()).into(productImage);

        switch (model.getOrderStatus()) {
            case "Ordered":
                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                orderedDate.setText(String.valueOf(model.getOrderedDate()));

                packegeIndicator.setVisibility(View.GONE);
                packedBody.setVisibility(View.GONE);
                packedDate.setVisibility(View.GONE);
                packedTitle.setVisibility(View.GONE);

                shippingIndicator.setVisibility(View.GONE);
                shippingBody.setVisibility(View.GONE);
                shippingDate.setVisibility(View.GONE);
                shippingTitle.setVisibility(View.GONE);

                deliveryIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                break;
            case "Packed":
                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                orderedDate.setText(String.valueOf(model.getOrderedDate()));

                packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                packedDate.setText(String.valueOf(model.getPackedDate()));

                O_P_progress.setProgress(100);

                P_S_progress.setVisibility(View.GONE);
                S_D_progress.setVisibility(View.GONE);

                shippingIndicator.setVisibility(View.GONE);
                shippingBody.setVisibility(View.GONE);
                shippingDate.setVisibility(View.GONE);
                shippingTitle.setVisibility(View.GONE);

                deliveryIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                break;
            case "Shipped":
                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                orderedDate.setText(String.valueOf(model.getOrderedDate()));

                packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                packedDate.setText(String.valueOf(model.getPackedDate()));

                shippingIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                shippingDate.setText(String.valueOf(model.getShippedDate()));

                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);

                S_D_progress.setVisibility(View.GONE);



                deliveryIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                break;
            case "Delivered":
                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                orderedDate.setText(String.valueOf(model.getOrderedDate()));

                packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                packedDate.setText(String.valueOf(model.getPackedDate()));

                shippingIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                shippingDate.setText(String.valueOf(model.getShippedDate()));


                deliveryIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                deliveredDate.setText(String.valueOf(model.getDeliverdDte()));

                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(100);

                case "Cancelled":

                    if (model.getPackedDate().after(model.getOrderedDate())) {

                        if (model.getShippedDate().after(model.getShippedDate())){
                            orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                            orderedDate.setText(String.valueOf(model.getOrderedDate()));

                            packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                            packedDate.setText(String.valueOf(model.getPackedDate()));

                            shippingIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                            shippingDate.setText(String.valueOf(model.getShippedDate()));


                            deliveryIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                            deliveredDate.setText(String.valueOf(model.getCancelledDate()));
                            deliveredTitle.setText("Cancelled");
                            deliveredBody.setText("Order Cancelled");

                            O_P_progress.setProgress(100);
                            P_S_progress.setProgress(100);
                            S_D_progress.setProgress(100);

                        }else {
                            orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                            orderedDate.setText(String.valueOf(model.getOrderedDate()));

                            packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                            packedDate.setText(String.valueOf(model.getPackedDate()));

                            shippingIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                            shippingDate.setText(String.valueOf(model.getCancelledDate()));
                            shippingTitle.setText("Cancelled");
                            shippingBody.setText("Order Cancelled");

                            O_P_progress.setProgress(100);
                            P_S_progress.setProgress(100);

                            S_D_progress.setVisibility(View.GONE);



                            deliveryIndicator.setVisibility(View.GONE);
                            deliveredBody.setVisibility(View.GONE);
                            deliveredDate.setVisibility(View.GONE);
                            deliveredTitle.setVisibility(View.GONE);
                        }
                    }else {
                        orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                        orderedDate.setText(String.valueOf(model.getOrderedDate()));

                        packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        packedDate.setText(String.valueOf(model.getCancelledDate()));
                        packedTitle.setText("Cancelled");
                        packedBody.setText("Order Cancelled");

                        O_P_progress.setProgress(100);

                        P_S_progress.setVisibility(View.GONE);
                        S_D_progress.setVisibility(View.GONE);

                        shippingIndicator.setVisibility(View.GONE);
                        shippingBody.setVisibility(View.GONE);
                        shippingDate.setVisibility(View.GONE);
                        shippingTitle.setVisibility(View.GONE);

                        deliveryIndicator.setVisibility(View.GONE);
                        deliveredBody.setVisibility(View.GONE);
                        deliveredDate.setVisibility(View.GONE);
                        deliveredTitle.setVisibility(View.GONE);
                    }

                break;
        }





    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}