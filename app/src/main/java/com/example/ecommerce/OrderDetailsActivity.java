package com.example.ecommerce;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {

    private int position;
    private TextView title,price,quantity;
    private ImageView productImage,orderIndicator,packegeIndicator,shippingIndicator,deliveryIndicator;
    private ProgressBar O_P_progress,P_S_progress,S_D_progress;
    private TextView orderedTitle,packedTitle,shippingTitle,deliveredTitle;
    private TextView orderedDate,packedDate,shippingDate,deliveredDate;
    private TextView orderedBody,packedBody,shippingBody,deliveredBody;
    private LinearLayout rateNowContainer;
    private int rating;
    private TextView fullName,Address,Pincode;
    private TextView totalItems,totalItemsPrice,deliveryPrice,totalAmount,savedAmount;
    private  Dialog loadingDialog;
    private SimpleDateFormat simpleDateFormat;

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

        ////loading dialog
        loadingDialog = new Dialog(OrderDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog

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
        rateNowContainer = findViewById(R.id.rate_now_container2);
        fullName = findViewById(R.id.fullname_shipping_D);
        Address = findViewById(R.id.address);
        Pincode = findViewById(R.id.pincode);
        totalItems = findViewById(R.id.total_items);
        totalItemsPrice = findViewById(R.id.total_items_price);
        deliveryPrice = findViewById(R.id.delivery_price);
        totalAmount = findViewById(R.id.total_price);
        savedAmount = findViewById(R.id.saved_amount);

        title.setText(model.getProductTitle());
        if (!model.getDiscountedPrice().equals("")) {
            price.setText("Rs."+model.getDiscountedPrice()+"/-");
        }else {
         price.setText("Rs."+model.getProductPrice()+"/-");
        }
        quantity.setText("Qty :"+String.valueOf(model.getProductQuantity()));
        Glide.with(this).load(model.getProductImage()).into(productImage);


        simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss aa");
        switch (model.getOrderStatus()) {
            case "Ordered":
                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

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
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

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
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippingIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                shippingDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

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
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippingIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                shippingDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));


                deliveryIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDeliverdDte())));

                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(100);

                case "Cancelled":

                    if (model.getPackedDate().after(model.getOrderedDate())) {

                        if (model.getShippedDate().after(model.getShippedDate())){
                            orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                            orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                            packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                            packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                            shippingIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                            shippingDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));


                            deliveryIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                            deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                            deliveredTitle.setText("Cancelled");
                            deliveredBody.setText("Order Cancelled");

                            O_P_progress.setProgress(100);
                            P_S_progress.setProgress(100);
                            S_D_progress.setProgress(100);

                        }else {
                            orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                            orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                            packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                            packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                            shippingIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                            shippingDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
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
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                        packegeIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
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


        //////rating layout
        rating = model.getRating();
        setReting(rating);
        for (int x = 0;x < rateNowContainer.getChildCount();x++){
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingDialog.show();
                    setReting(starPosition);
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document(model.getProductId());

                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                        @Nullable
                        @Override
                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                            if (rating != 0){
                                Long  increase = documentSnapshot.getLong(starPosition +1+ "_star") + 1;
                                Long  decrease = documentSnapshot.getLong(rating +1+ "_star") - 1;
                                transaction.update(documentReference,starPosition +1+ "_star",increase);
                                transaction.update(documentReference,rating+1 + "_star",decrease);
                            }else {
                                Long  increase = documentSnapshot.getLong(starPosition +1+ "_star") + 1;
                                transaction.update(documentReference,starPosition+1 + "_star",increase);
                            }
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Object>() {

                        @Override
                        public void onSuccess(Object object) {
                            Map<String, Object> myRating = new HashMap<>();
                            if (DBqueries.myRatedIds.contains(model.getProductId())) {
                                myRating.put("rating_" + DBqueries.myRatedIds.indexOf(model.getProductId()), (long) starPosition + 1);
                            } else {
                                myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                myRating.put("product_ID_" + DBqueries.myRatedIds.size(), model.getProductId());
                                myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                            }

                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                DBqueries.myOrderItemModelList.get(position).setRating(starPosition);
                                                if (DBqueries.myRatedIds.contains(model.getProductId())){
                                                    DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(model.getProductId()),Long.parseLong(String.valueOf(starPosition+1))); ;
                                                }else {
                                                    DBqueries.myRatedIds.add(model.getProductId());
                                                    DBqueries.myRating.add(Long.parseLong(String.valueOf(starPosition+1)));
                                                }
                                            }else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(OrderDetailsActivity.this,error, Toast.LENGTH_SHORT).show();
                                            }
                                            loadingDialog.dismiss();
                                        }
                                    });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismiss();
                        }
                    });
                }
            });
        }
        //////rating layout

        fullName.setText(model.getFullName());
        Address.setText(model.getAddress());
        Pincode.setText(model.getPincode());
        totalItems.setText("Price("+model.getProductQuantity()+"items)");


        Long totalItemPricValue;

        if (model.getDiscountedPrice().equals("")){
            totalItemPricValue = model.getProductQuantity()*Long.valueOf(model.getProductPrice());
            totalItemsPrice.setText("Rs."+totalItemPricValue+"/-");
        }else {
            totalItemPricValue = model.getProductQuantity()*Long.valueOf(model.getDiscountedPrice());
            totalItemsPrice.setText("Rs."+ totalItemsPrice +"/-");
        }
        if (model.getDeliverPrice().equals("FREE")){
            deliveryPrice.setText(model.getDeliverPrice());
            totalAmount.setText(totalItemsPrice.getText());
        }else {
            deliveryPrice.setText("Rs." + model.getDeliverPrice() + "/-");
            totalAmount.setText("Rs."+ (totalItemPricValue + Long.valueOf( model.getDeliverPrice()))+"/-");

        }

        if (!model.getCuttedPrice().equals("")){
            if (!model.getDiscountedPrice().equals("")){
                savedAmount.setText("You saved Rs."+ model.getProductQuantity() * (Long.valueOf( model.getCuttedPrice()) - Long.valueOf(model.getDiscountedPrice()))+"on this order");
            }else {
                savedAmount.setText("You saved Rs."+ model.getProductQuantity() * (Long.valueOf( model.getCuttedPrice()) - Long.valueOf(model.getProductPrice()))+"on this order");

            }
        }else {
            if (!model.getDiscountedPrice().equals("")){
                savedAmount.setText("You saved Rs."+ model.getProductQuantity() * (Long.valueOf( model.getProductPrice()) - Long.valueOf(model.getDiscountedPrice()))+"on this order");

            }else {
                savedAmount.setText("You saved Rs. 0/- on this order");

            }
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

    private void setReting(int starPosition) {
        for (int x = 0;x < rateNowContainer.getChildCount();x++){
            ImageView starBtn = (ImageView)rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }
}