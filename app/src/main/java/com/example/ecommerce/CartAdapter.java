package com.example.ecommerce;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** @noinspection ALL*/
public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;


    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override//usha

    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new CartItemViewholder(cartItemView);

            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }
    }
    private int safeParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException | NullPointerException e) {
            return 0; // Return a default value (0) if parsing fails
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupens = cartItemModelList.get(position).getFreeCoupens();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();
                Long productQuantity = cartItemModelList.get(position).getProductQuentity();
                Long maxQuantity = cartItemModelList.get(position).getMaxQuentity();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIDs();
                long stockQty = cartItemModelList.get(position).getStockQuentity();
                boolean COD = cartItemModelList.get(position).isCOD();

                ((CartItemViewholder) viewHolder).setItemDetails(productID, resource, title, freeCoupens, productPrice, cuttedPrice, offersApplied, position, inStock, String.valueOf(productQuantity), maxQuantity, qtyError,qtyIds,stockQty,COD);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliverPrice;
                int totalAmount;
                int saveAmount = 0;
                for (int x = 0; x < cartItemModelList.size(); x++) {
                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()) {
                        int quantity = safeParseInt(String.valueOf(cartItemModelList.get(x).getProductQuentity()));
                        totalItems  = totalItems + quantity;
                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                            totalItemPrice = totalItemPrice + safeParseInt(cartItemModelList.get(x).getProductPrice()) * quantity;
                        }else {
                            totalItemPrice = totalItemPrice + safeParseInt(cartItemModelList.get(x).getDiscountedPrice()) * quantity;
                        }
                        if (!TextUtils.isEmpty(cartItemModelList.get(x).getCuttedPrice())) {
                            saveAmount = saveAmount + (safeParseInt(cartItemModelList.get(x).getCuttedPrice()) - safeParseInt(cartItemModelList.get(x).getProductPrice())) * quantity;
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                saveAmount = saveAmount + (safeParseInt(cartItemModelList.get(x).getProductPrice()) - safeParseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        }else {
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                saveAmount = saveAmount + (safeParseInt(cartItemModelList.get(x).getProductPrice()) - safeParseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        }
                    }
                }
                if (totalItemPrice > 500) {
                    deliverPrice = "Free";
                    totalAmount = totalItemPrice;
                } else {
                    deliverPrice = "50";
                    totalAmount = totalItemPrice + 50;
                }

                cartItemModelList.get(position).setTotalItems(totalItems);
                cartItemModelList.get(position).setTotalItemPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliverPrice(deliverPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSaveAmount(saveAmount);
                ((CartTotalAmountViewholder) viewHolder).setTotalAmount(totalItems, totalItemPrice, deliverPrice, totalAmount, saveAmount);
                break;
            default:
                return;
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView freeCoupensIcon;
        private TextView productTitle;
        private TextView freecoupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView coupensApplied;
        private TextView productQuantity;
        private LinearLayout coupenRedemptionLayout;
        private TextView coupenRedemptionBody;
        private ImageView codIndicator;
        // Coupen dialog views
        private TextView coupentitle, coupenExpiryDate, coupenBody;
        private RecyclerView coupensRecyclerView;
        private TextView originalPrice;
        private TextView discountedPrice;
        private  LinearLayout selectedCoupen;
        private Button removeCoupenBtn;
        private Button applyCoupenBtn;
        private LinearLayout deleteBtn;
        private Button redeemBtn;
        private LinearLayout applyORremoveBtnContaine;
        private TextView footerText;
        private String productOriginalPrice;



        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image5);
            productTitle = itemView.findViewById(R.id.products_title);
            freeCoupensIcon = itemView.findViewById(R.id.free_coupen_icon);
            freecoupens = itemView.findViewById(R.id.tv_free_coupen);
            productPrice = itemView.findViewById(R.id.products_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_prices);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            coupensApplied = itemView.findViewById(R.id.coupens_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            coupenRedemptionLayout = itemView.findViewById(R.id.coupen_redemption_layout);
            redeemBtn = itemView.findViewById(R.id.coupen_redemption_btn);
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
            coupenRedemptionBody = itemView.findViewById(R.id.tv_coupen_redemption);
            codIndicator = itemView.findViewById(R.id.cod_indicator);

        }

        private void setItemDetails(String productId, String resource, String title, Long freecoupensNo, String productPriceText, String cuttedPriceText, Long offersAppliedNo, int position, boolean inStock, String Quantity, Long maxQuantity, boolean qtyError,List<String> qtyIds,long stockQty,boolean COD) {
                Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(productImage);
                productTitle.setText(title);

            final Dialog checkCoupenPriceDialog = new Dialog(itemView.getContext());
            checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
            checkCoupenPriceDialog.setCancelable(false);
            checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (COD){
                codIndicator.setVisibility(View.VISIBLE);
            }else {
                codIndicator.setVisibility(View.INVISIBLE);
            }


            if (inStock) {
                if (freecoupensNo > 0) {
                    freeCoupensIcon.setVisibility(View.VISIBLE);
                    freecoupens.setVisibility(View.VISIBLE);
                    if (freecoupensNo == 1) {
                        freecoupens.setText("free " + freecoupensNo + " Coupen");
                    } else {
                        freecoupens.setText("free " + freecoupensNo + " Coupens");
                    }
                } else {
                    freeCoupensIcon.setVisibility(View.INVISIBLE);
                    freecoupens.setVisibility(View.INVISIBLE);
                }
                    productPrice.setText("Rs." + productPriceText + "/-");
                    productPrice.setTextColor(Color.parseColor("#000000"));
                    cuttedPrice.setText("Rs." + cuttedPriceText + "/-");
                    coupenRedemptionLayout.setVisibility(View.VISIBLE);


                    // Coupen dialog setup

                    ImageView toggleRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
                    coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
                    selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);
                    coupentitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
                    coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_validity);
                    coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);
                    originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
                    footerText = checkCoupenPriceDialog.findViewById(R.id.footer_text);
                    applyORremoveBtnContaine = checkCoupenPriceDialog.findViewById(R.id.apply_or_remove_btns_container);
                    discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);
                    removeCoupenBtn = checkCoupenPriceDialog.findViewById(R.id.remove_btn);
                    applyCoupenBtn = checkCoupenPriceDialog.findViewById(R.id.apply_btn);
                    footerText.setVisibility(View.GONE);
                    applyORremoveBtnContaine.setVisibility(View.VISIBLE);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    coupensRecyclerView.setLayoutManager(layoutManager);
                    originalPrice.setText(productPrice.getText());
                    productOriginalPrice = productPriceText;

                    MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(position,DBqueries.rewardModelList, true, coupensRecyclerView, selectedCoupen,productOriginalPrice, coupentitle, coupenExpiryDate, coupenBody, discountedPrice,cartItemModelList);
                    coupensRecyclerView.setAdapter(myRewardsAdapter);
                    myRewardsAdapter.notifyDataSetChanged();


                    applyCoupenBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                                for (RewardModel rewardModel : DBqueries.rewardModelList) {
                                    if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                        rewardModel.setAlreadyUsed(true);
                                        coupenRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.rewad_gradient_backgruond));
                                        coupenRedemptionBody.setText(rewardModel.getCoupenBody());
                                        redeemBtn.setText("Coupen");
                                    }
                                }
                                coupensApplied.setVisibility(View.VISIBLE);
                                cartItemModelList.get(position).setDiscountedPrice(discountedPrice.getText().toString().substring(3,discountedPrice.getText().length() - 2));
                                productPrice.setText(discountedPrice.getText());
                                String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(discountedPrice.getText().toString().substring(3,discountedPrice.getText().length() - 2)));
                                coupensApplied.setText("Coupemn applied Rs." + offerDiscountedAmt);
                                notifyItemChanged(cartItemModelList.size() - 1);
                                checkCoupenPriceDialog.dismiss();
                            }

                        }

                    });

                    removeCoupenBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (RewardModel rewardModel : DBqueries.rewardModelList){
                                if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())){
                                    rewardModel.setAlreadyUsed(false);
                                }
                            }
                            coupentitle.setText("Coupen");
                            coupenExpiryDate.setText("validity");
                            coupenBody.setText("Tap the icon on the top right corner to selecte and  redeem the coupen");
                            coupensApplied.setVisibility(View.INVISIBLE);
                            coupenRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                            coupenRedemptionBody.setText("Apply Your Coupen here");
                            redeemBtn.setText("Redeem");
                            cartItemModelList.get(position).setSelectedCoupenId(null);
                            notifyItemChanged(cartItemModelList.size() - 1);
                            checkCoupenPriceDialog.dismiss();
                        }
                    });

                    toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialogRecyclerView();
                        }
                    });

                    if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                        for (RewardModel rewardModel : DBqueries.rewardModelList) {
                            if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                coupenRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.rewad_gradient_backgruond));
                                coupenRedemptionBody.setText(rewardModel.getCoupenBody());
                                redeemBtn.setText("Coupen");
                                coupenBody.setText(rewardModel.getCoupenBody());

                                if (rewardModel.getType().equals("Discount")) {
                                    coupentitle.setText(rewardModel.getType());
                                } else {
                                    coupentitle.setText("FLAT Rs." + rewardModel.getdiscORamt()+ "/- OFF");
                                }
                                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                                coupenExpiryDate.setText("Till " + simpleDateFormat.format(rewardModel.getTimestamp()));

                            }
                        }

                        discountedPrice.setText("Rs."+cartItemModelList.get(position).getDiscountedPrice()+"/-");
                        coupensApplied.setVisibility(View.VISIBLE);
                        productPrice.setText("Rs."+cartItemModelList.get(position).getDiscountedPrice()+"/-");
                        String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(cartItemModelList.get(position).getDiscountedPrice()));
                        coupensApplied.setText("Coupemn applied Rs." + offerDiscountedAmt+"/-");
                    }else {
                        coupensApplied.setVisibility(View.INVISIBLE);
                        coupenRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                        coupenRedemptionBody.setText("Apply Your Coupen here");
                        redeemBtn.setText("Redeem");
                    }

                    /////// End of coupen dialog

                    productQuantity.setText("Qty: " + Quantity);

                    if (!showDeleteBtn) {

                        if (qtyError) {
                            productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                            productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorRed)));
                        } else {
                            productQuantity.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
                            productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(android.R.color.black)));
                        }
                    }

                    productQuantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog quantityDialog = new Dialog(itemView.getContext());
                            quantityDialog.setContentView(R.layout.quantity_dialog);
                            quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            quantityDialog.setCancelable(false);

                            EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                            Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                            Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
                            quantityNo.setHint("Max: " + String.valueOf(maxQuantity));

                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    quantityDialog.dismiss();
                                }
                            });

                            okBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!TextUtils.isEmpty(quantityNo.getText())) {
                                        if (Long.valueOf(quantityNo.getText().toString()) <= maxQuantity && Long.valueOf(quantityNo.getText().toString()) != 0) {

                                            if (itemView.getContext() instanceof MainActivity) {
                                               cartItemModelList.get(position).setProductQuentity(Long.valueOf(quantityNo.getText().toString()));
                                            } else {
                                                if (DeliveryActivity.fromCart) {
                                                    cartItemModelList.get(position).setProductQuentity(Long.valueOf(quantityNo.getText().toString()));
                                                } else {
                                                    DeliveryActivity.cartItemModelList.get(position).setProductQuentity(Long.valueOf(quantityNo.getText().toString()));
                                                }
                                            }
                                            productQuantity.setText("Qty: " + quantityNo.getText());
                                            notifyItemChanged(cartItemModelList.size() - 1);
                                            if (!showDeleteBtn){
                                                DeliveryActivity.loadingDialog.show();
                                                DeliveryActivity.cartItemModelList.get(position).setQtyError(false);
                                                int initialQty = Integer.parseInt(Quantity);
                                                int finalQty = Integer.parseInt(quantityNo.getText().toString());
                                                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                                                if (finalQty > initialQty) {

                                                    for (int y = 0; y < finalQty - initialQty; y++) {
                                                        String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                                                        Map<String, Object> timestamp = new HashMap<>();
                                                        timestamp.put("time", FieldValue.serverTimestamp());
                                                        int finalY = y;
                                                        firebaseFirestore.collection("PRODUCTS").document(productId).collection("QUANTITY").document(quantityDocumentName).set(timestamp)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void avoid) {
                                                                       qtyIds.add(quantityDocumentName);
                                                                        if (finalY + 1 == finalQty - initialQty) {
                                                                            firebaseFirestore.collection("PRODUCTS").document(productId).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(stockQty).get()
                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                List<String> serverQuantity = new ArrayList<>();
                                                                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                                    serverQuantity.add(queryDocumentSnapshot.getId());
                                                                                                }
                                                                                                long availableQty = 0;
                                                                                                for (String qtyId : qtyIds) {

                                                                                                    if (!serverQuantity.contains(qtyId)) {
                                                                                                           DeliveryActivity.cartItemModelList.get(position).setQtyError(true);
                                                                                                           DeliveryActivity.cartItemModelList.get(position).setMaxQuentity(availableQty);
                                                                                                            Toast.makeText(itemView.getContext(), " Sorry ! All products may not be available is required quantity......", Toast.LENGTH_SHORT).show(); 
                                                                                                    }else {
                                                                                                        availableQty++;
                                                                                                    }
                                                                                                }
                                                                                               DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                            } else {
                                                                                                String error = task.getException().getMessage();
                                                                                                Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                                ////error
                                                                                            }
                                                                                            DeliveryActivity.loadingDialog.dismiss();
                                                                                        }
                                                                                    });

                                                                        }

                                                                    }
                                                                });
                                                    }
                                                }else if (initialQty > finalQty){

                                                    for (int x = 0;x < initialQty - finalQty;x++) {
                                                         String qtyId = qtyIds.get(qtyIds.size() - 1 - x);
                                                        final int finalX = x;
                                                        firebaseFirestore.collection("PRODUCTS").document(productId).collection("QUANTITY").document(qtyId).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        qtyIds.remove(qtyId);
                                                                        DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                        if (finalX + 1 ==  initialQty - finalQty){
                                                                            DeliveryActivity.loadingDialog.dismiss();
                                                                        }
                                                                    }
                                                                });
                                                    }

                                                }
                                            }


                                        } else {
                                            Toast.makeText(itemView.getContext(), "max quantity is " + maxQuantity.toString(), Toast.LENGTH_SHORT).show(); //Quantity cannot be greater than ", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    quantityDialog.dismiss();


                                }
                            });
                            quantityDialog.show();
                        }
                    });
                    if (offersAppliedNo > 0) {
                        offersApplied.setVisibility(View.VISIBLE);
                        String offerDiscountedAmt = String.valueOf(Long.valueOf(cuttedPriceText) - Long.valueOf(productPriceText));
                        offersApplied.setText("Offer Applied Rs." + offerDiscountedAmt + "/-");
                    } else {
                        offersApplied.setVisibility(View.INVISIBLE);
                    }

                } else {
                    productPrice.setText("Out of Stock");
                    productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.lavender));
                    cuttedPrice.setText("");
                    coupenRedemptionLayout.setVisibility(View.GONE);
                    freecoupens.setVisibility(View.GONE);
                    productQuantity.setVisibility(View.INVISIBLE);
                    coupensApplied.setVisibility(View.GONE);
                    offersApplied.setVisibility(View.GONE);
                    freeCoupensIcon.setVisibility(View.INVISIBLE);
                }
                if (Boolean.TRUE.equals(showDeleteBtn)) {
                    deleteBtn.setVisibility(View.VISIBLE);
                } else {
                    deleteBtn.setVisibility(View.GONE);
                }



                redeemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (RewardModel rewardModel : DBqueries.rewardModelList){
                            if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())){
                                rewardModel.setAlreadyUsed(false);
                            }
                        }
                        checkCoupenPriceDialog.show();

                    }
                });

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                            for (RewardModel rewardModel : DBqueries.rewardModelList){
                                if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())){
                                    rewardModel.setAlreadyUsed(false);
                                }
                            }
                        }
                        if (!ProductDetailsActivity.running_cart_query) {
                            ProductDetailsActivity.running_cart_query = true;
                            DBqueries.removeFromCart(position, itemView.getContext(), cartTotalAmount);
                        }
                    }
                });

            }
        private void showDialogRecyclerView() {
            if (coupensRecyclerView.getVisibility() == View.GONE) {
                coupensRecyclerView.setVisibility(View.VISIBLE);
                selectedCoupen.setVisibility(View.GONE);
            } else {
                coupensRecyclerView.setVisibility(View.GONE);
                selectedCoupen.setVisibility(View.VISIBLE);
            }
        }
        }

        class CartTotalAmountViewholder extends RecyclerView.ViewHolder {

            private TextView totalItems;
            private TextView totalItemPrice;
            private TextView deliveryPrice;
            private TextView totalAmount;
            private TextView savedAmount;

            public CartTotalAmountViewholder(@NonNull View itemView) {
                super(itemView);
                totalItems = itemView.findViewById(R.id.total_items);
                totalItemPrice = itemView.findViewById(R.id.total_items_price);
                deliveryPrice = itemView.findViewById(R.id.delivery_price);
                totalAmount = itemView.findViewById(R.id.total_price);
                savedAmount = itemView.findViewById(R.id.saved_amount);
            }

            private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int savedAmountText) {
                totalItems.setText("Price (" + totalItemText + "items)");
                totalItemPrice.setText("Rs." + totalItemPriceText + "/-");
                if (deliveryPriceText.equals("Free")) {
                    deliveryPrice.setText(deliveryPriceText);
                } else {
                    deliveryPrice.setText("Rs." + deliveryPriceText + "/-");
                }
                totalAmount.setText("Rs." + totalAmountText + "/-");
                cartTotalAmount.setText("Rs." + totalAmountText + "/-");
                savedAmount.setText("You saved Rs." + savedAmountText + "/- on this order");

                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                if (totalItemPriceText == 0) {
                    if (DeliveryActivity.fromCart) {
                        cartItemModelList.remove(cartItemModelList.size() - 1);
                        DeliveryActivity.cartItemModelList.remove(DeliveryActivity.cartItemModelList.size() - 1);
                    }
                    if (Boolean.TRUE.equals(showDeleteBtn)){
                        cartItemModelList.remove(cartItemModelList.size() - 1);
                    }
                    parent.setVisibility(View.GONE);
                } else {
                    parent.setVisibility(View.VISIBLE);
                }
            }
        }
    }
