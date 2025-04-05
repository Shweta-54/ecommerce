package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class DeliveryActivity extends AppCompatActivity implements PaymentResultListener {

    public static final int SELECT_ADDRESS = 0;
    private Button continuetBtn;
    private ConstraintLayout orderConfirmationLayout;
    private ImageButton continueShoppingBtn;
    private TextView orderId;
    private TextView totalAmount;
    private TextView fullName;
    private String name, mobileNo;
    private TextView fullAddress;
    private TextView pincode;
    private String paymentMethod = "RazorPay";
    public static List<CartItemModel> cartItemModelList;
    public static CartAdapter cartAdapter;
    public static Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private  ImageButton payment;
    private ImageView cod;
    private Boolean successResponse = false;
    public static Boolean fromCart;
    Button changeORaddNewAddressbtn;
    private String s = UUID.randomUUID().toString().substring(0,28);;
    public static boolean codOrderConfirmed = false;
    private TextView codTitle;
    private View divider;

    private FirebaseFirestore firebaseFirestore;
    public static boolean getQtyIDs = true;



    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Checkout.preload(getApplicationContext());
        // Subscribe to FCM topic
        FirebaseMessaging.getInstance().subscribeToTopic("order_confirmation")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "welcome to  Order Confirmation!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Subscription Failed!", Toast.LENGTH_SHORT).show();
                    }
                });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");


        RecyclerView deliveryRecyclerView = findViewById(R.id.delivery_recyclerview);
        changeORaddNewAddressbtn = findViewById(R.id.change_or_add_address_btn);
        continuetBtn = findViewById(R.id.cart_continue_btn);
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        orderId = findViewById(R.id.order_id);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullName = findViewById(R.id.fullname_shipping_D);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);


        ////loading dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog


        ////payment dialog
        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        payment = paymentMethodDialog.findViewById(R.id.paytm);
        cod = paymentMethodDialog.findViewById(R.id.cod_btn);
        codTitle = paymentMethodDialog.findViewById(R.id.cod_btn_title);
        divider = paymentMethodDialog.findViewById(R.id.divider);
        ////payment dialog

        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIDs = true;


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);


        cartAdapter = new CartAdapter(cartItemModelList, totalAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeORaddNewAddressbtn.setVisibility(View.VISIBLE);
        changeORaddNewAddressbtn.setOnClickListener(v -> {
            getQtyIDs = false;
            Intent myAddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
            myAddressesIntent.putExtra("MODE", SELECT_ADDRESS);
            startActivity(myAddressesIntent);
        });

        continuetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean allProductsAvailable = true;
                for (CartItemModel cartItemModel : cartItemModelList) {
                    if (cartItemModel.isQtyError()) {
                        allProductsAvailable = false;
                        break;
                    }
                    if (cartItemModel.getType() == CartItemModel.CART_ITEM) {
                        if (!cartItemModel.isCOD()) {
                            cod.setEnabled(false);
                            cod.setAlpha(0.5f);
                            codTitle.setAlpha(0.5f);
                            divider.setVisibility(View.GONE);
                            break;
                        } else {
                            cod.setEnabled(true);
                            cod.setAlpha(1f);
                            codTitle.setAlpha(1f);
                            divider.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (allProductsAvailable){
                    paymentMethodDialog.show();
                }
            }
        });


        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "COD";
                placeOrderDetails();
            }
        });



        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "RazorPay";
                placeOrderDetails();
            }
        });
    }

//    private void startPayment() {
//
//    }

    public void onPaymentSuccess(String razorpayPaymentId) {
        runOnUiThread(() -> {
            loadingDialog.show();

            // 1. First verify payment with Razorpay
            verifyPaymentWithRazorpay(razorpayPaymentId, new PaymentVerificationCallback() {
                @Override
                public void onVerified(boolean isSuccessful) {
                    if (isSuccessful) {
                        // 2. Only update Firestore if payment is verified
                        Map<String,Object> updateStatus = new HashMap<>();
                        updateStatus.put("Payment_Status","Paid");
                        updateStatus.put("Order_Status","Ordered");
                        updateStatus.put("razorpay_payment_id", razorpayPaymentId);
                        firebaseFirestore.collection("ORDERS").document(s)
                                .update(updateStatus)
                                .addOnCompleteListener(task -> {
                                    loadingDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Map<String,Object> userOrder = new HashMap<>();
                                        userOrder.put("ORDER_ID",s);
                                        userOrder.put("time",FieldValue.serverTimestamp());
                                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(s)
                                                .set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            showConfirmationLayout();
                                                        }else {
                                                            Toast.makeText(DeliveryActivity.this, "failed to update user order list", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(DeliveryActivity.this,
                                                "Payment verified but order update failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(DeliveryActivity.this,
                                "Payment verification failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    // Payment verification interface
    interface PaymentVerificationCallback {
        void onVerified(boolean isSuccessful);
    }

    // Method to verify payment with Razorpay servers
    private void verifyPaymentWithRazorpay(String paymentId, PaymentVerificationCallback callback) {
        // Implement actual Razorpay verification API call here
        // This is just a mock implementation
        new Thread(() -> {
            try {
                // Simulate network call
                Thread.sleep(1500);
                // Assume verification succeeds
                runOnUiThread(() -> callback.onVerified(true));
            } catch (Exception e) {
                runOnUiThread(() -> callback.onVerified(false));
            }
        }).start();
    }
    public void onPaymentError(int i, String s) {
        loadingDialog.dismiss();
        Toast.makeText(this, "Payment failed: " + s, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //accessing quantity
        if (getQtyIDs) {
            loadingDialog.show();
            for (int x = 0; x < DBqueries.cartItemModelList.size() - 1; x++) {


              for (int y = 0;y < cartItemModelList.get(x).getProductQuentity();y++){
                  String quantityDocumentName =  UUID.randomUUID().toString().substring(0,20);
                  Map<String,Object> timestamp = new HashMap<>();
                  timestamp.put("time", FieldValue.serverTimestamp());
                  int finalX = x;
                  int finalY = y;
                  firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(quantityDocumentName).set(timestamp)
                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if (task.isSuccessful()){

                                              cartItemModelList.get(finalX).getQtyIDs().add(quantityDocumentName);
                                              if (finalY + 1 == cartItemModelList.get(finalX).getProductQuentity()){
                                                  firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductID()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(cartItemModelList.get(finalX).getStockQuentity()).get()
                                                          .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                  if (task.isSuccessful()){
                                                                      List<String> serverQuantity = new ArrayList<>();

                                                                      for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                                                          serverQuantity.add(queryDocumentSnapshot.getId());
                                                                      }

                                                                      long availableQty = 0;
                                                                      boolean nolongerAvailable = true;
                                                                      for (String qtyId : cartItemModelList.get(finalX).getQtyIDs()){
                                                                          cartItemModelList.get(finalX).setQtyError(false);

                                                                          if (!serverQuantity.contains(qtyId)) {
                                                                              if (nolongerAvailable){
                                                                                  cartItemModelList.get(finalX).setInStock(false);
                                                                              }else {
                                                                                  cartItemModelList.get(finalX).setQtyError(true);
                                                                                  cartItemModelList.get(finalX).setMaxQuentity(availableQty);
                                                                                  Toast.makeText(DeliveryActivity.this, " Sorry ! All products may not be available is required quantity......", Toast.LENGTH_SHORT).show();
                                                                              }
                                                                          }else {
                                                                              availableQty++;
                                                                              nolongerAvailable = false;
                                                                          }
                                                                      }
                                                                      cartAdapter.notifyDataSetChanged();

                                                                  }else {
                                                                      String error = task.getException().getMessage();
                                                                      Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                      ////error
                                                                  }
                                                                  loadingDialog.dismiss();
                                                              }
                                                          });
                                              }

                                          }else {
                                              loadingDialog.dismiss();
                                              String error = task.getException().getMessage();
                                              Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                          }
                              }
                          });
              }
            }
        }else {
            getQtyIDs = true;
        }

        name = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullname();
        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        fullName.setText(name + " / " + mobileNo);
        fullAddress.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

        if (codOrderConfirmed) {
            showConfirmationLayout();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();

        if (getQtyIDs) {

            for (int x = 0; x < DBqueries.cartItemModelList.size() - 1; x++) {

                if (!successResponse) {
                    for (String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                        int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (qtyID.equals(cartItemModelList.get(finalX).getQtyIDs().get(cartItemModelList.get(finalX).getQtyIDs().size() - 1 ))){
                                            cartItemModelList.get(finalX).getQtyIDs().clear();



                                        }
                                    }
                                });
                    }
                }else {
                    cartItemModelList.get(x).getQtyIDs().clear();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (successResponse){
            finish();
            return;
        }
        super.onBackPressed();
    }

    private void showConfirmationLayout() {
        successResponse = true;
        codOrderConfirmed = false;
        getQtyIDs = false;
        for (int x = 0;x < DBqueries.cartItemModelList.size() - 1;x++){

            for (String qtyID : cartItemModelList.get(x).getQtyIDs()){

                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("user_ID", FirebaseAuth.getInstance().getUid());
            }
        }
        if (MainActivity.mainActivity != null) {
            MainActivity.mainActivity.finish();
            MainActivity.mainActivity = null;
            MainActivity.showCart = false;
        }else {
           MainActivity.resetMainActivity = true;
        }
        if (ProductDetailsActivity.productDetailsActivity != null){
            ProductDetailsActivity.productDetailsActivity.finish();
            ProductDetailsActivity.productDetailsActivity = null;
        }

        if (fromCart){
            loadingDialog.show();
            Map<String, Object> updateCartList = new HashMap<>();
            long cartListSize = 0;
            List<Integer> indexList = new ArrayList<>();
            for (int x = 0; x < DBqueries.cartList.size(); x++) {
                if (!cartItemModelList.get(x).isInStock()){
                    updateCartList.put("product_ID_" + cartListSize, cartItemModelList.get(x).getProductID());
                    cartListSize++;
                }else {
                    indexList.add(x);
                }
            }
            updateCartList.put("list_size",cartListSize);

            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                for (int x = 0;x < indexList.size();x++){
                                    DBqueries.cartList.remove(indexList.get(x).intValue());
                                    DBqueries.cartItemModelList.remove(indexList.get(x).intValue());
                                    DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size() - 1);
                                }
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });

        }
        continuetBtn.setEnabled(false);
        changeORaddNewAddressbtn.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderConfirmationLayout.setVisibility(View.VISIBLE);
        // Retrieve the order ID passed from OTPverificationActivity or Razorpay
        String receivedOrderId = getIntent().getStringExtra("orderId");
        if (receivedOrderId != null) {
            orderId.setText("Order ID: " + receivedOrderId);
        } else {
            orderId.setText("Order ID: " + s); // Use the order ID from Razorpay payment
        }

        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
    private void sendOrderConfirmationMessage(String orderId) {
        String message = "Your order " + orderId + " has been successfully confirmed. Thank you for shopping with us!";

        FirebaseMessaging.getInstance().subscribeToTopic("order_confirmation")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Log the message or send it to the server
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to send confirmation", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void placeOrderDetails() {
        String userId = FirebaseAuth.getInstance().getUid();
        loadingDialog.show();
        for (CartItemModel cartItemModel: cartItemModelList) {
            if (cartItemModel.getType() == cartItemModel.CART_ITEM){
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER_ID",s);
                orderDetails.put("Product_Id",cartItemModel.getProductID());
                orderDetails.put("Product_Image",cartItemModel.getProductImage());
                orderDetails.put("Product_Title",cartItemModel.getProductTitle());
                orderDetails.put("User_Id",userId);
                orderDetails.put("Product_Quantity",cartItemModel.getProductQuentity());
                if (cartItemModel.getCuttedPrice() != null){
                    orderDetails.put("Cutted_Price", cartItemModel.getCuttedPrice());
                }else {
                    orderDetails.put("Cutted_Price", "");
                }
                orderDetails.put("Product_Price",cartItemModel.getProductPrice());
                if (cartItemModel.getSelectedCoupenId() != null) {
                    orderDetails.put("Coupen_Id", cartItemModel.getSelectedCoupenId());
                }else {
                    orderDetails.put("Coupen_Id", "");
                }
                if (cartItemModel.getDiscountedPrice() != null) {
                    orderDetails.put("Discounted_Price", cartItemModel.getDiscountedPrice());
                }else {
                    orderDetails.put("Discounted_Price", "");
                }
                orderDetails.put("Ordered date",FieldValue.serverTimestamp());
                orderDetails.put("Packed date",FieldValue.serverTimestamp());
                orderDetails.put("Shipped date",FieldValue.serverTimestamp());
                orderDetails.put("Deliverd date",FieldValue.serverTimestamp());
                orderDetails.put("Cancelled date",FieldValue.serverTimestamp());
                orderDetails.put("Order_Status","Ordered");
                orderDetails.put("Payment_Method",paymentMethod);
                orderDetails.put("Address",fullAddress.getText());
                orderDetails.put("FullName",fullName.getText());
                orderDetails.put("Pinecode",pincode.getText());
                orderDetails.put("Free_Coupen",cartItemModel.getFreeCoupens());
                orderDetails.put("Delivery Price",cartItemModelList.get(cartItemModelList.size() - 1).getDeliverPrice());
                orderDetails.put("Cancellation requested",false);
                firebaseFirestore.collection("ORDERS").document(s).collection("OrderItems").document(cartItemModel.getProductID())
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()){
                                    String error = task.getException().getMessage();
                                    Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else {
                Map<String,Object> orderDetails = new HashMap<>();
                orderDetails.put("Total Items",cartItemModel.getTotalItems());
                orderDetails.put("Total Items Price",cartItemModel.getTotalItemPrice());
                orderDetails.put("Delivery Price",cartItemModel.getDeliverPrice());
                orderDetails.put("Total Amount",cartItemModel.getTotalAmount());
                orderDetails.put("Saved Amount",cartItemModel.getSaveAmount());
                orderDetails.put("Payment_Status","not paid");
                orderDetails.put("Order_Status","Canceled");
                firebaseFirestore.collection("ORDERS").document(s)
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    if (paymentMethod.equals("RazorPay")){
                                        Razorpay();
                                    }else {
                                        cod();
                                    }
                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }

        }


    }

    private void Razorpay(){
        getQtyIDs = false;
        paymentMethodDialog.dismiss();
        loadingDialog.show();

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_jZwGiGMqlzJhaU");
        checkout.setImage(R.drawable.splash);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "Razorpay Demo");
            jsonObject.put("description", "if you like this app, you can buy me a cup of coffee");
            jsonObject.put("image", "https://your-logo-url.com");
//            jsonObject.put("order_id", orderId);
            jsonObject.put("theme.color", "#3399cc");
            jsonObject.put("currency", "INR");
            jsonObject.put("amount", totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2));

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 10);
            jsonObject.put("retry", retryObj);

            checkout.open(DeliveryActivity.this, jsonObject);




        } catch (Exception e) {
            loadingDialog.dismiss();
            Toast.makeText(DeliveryActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }
    private void cod(){
        getQtyIDs = false;
        paymentMethodDialog.dismiss();
        sendOrderConfirmationMessage(orderId.getText().toString());
        Intent otpIntent = new Intent(DeliveryActivity.this, OTPverificationActivity.class);
        otpIntent.putExtra("mobileNo", mobileNo.substring(0,10));
        otpIntent.putExtra("ORDER_ID", s);
        startActivity(otpIntent);

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Ensure any previous dialogs are dismissed
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        loadingDialog = null;
    }




}