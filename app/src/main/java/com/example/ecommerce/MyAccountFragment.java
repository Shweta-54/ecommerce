package com.example.ecommerce;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyAccountFragment extends Fragment {

    public MyAccountFragment() {
        // Required empty public constructor
    }

    private FloatingActionButton settingsBtn;
    public static final int MANAGE_ADDRESS = 1;
    private CircleImageView profileView, currentOrderImage;
    private TextView name,email,tvCurrentOrderStatus;
    private LinearLayout layoutContainer,recentOrdersContainer;
    private Dialog loadingDialog;
    private ImageView orderIndicator,packedIndicator,shippendIndicator,deliveredIndicator;
    private ProgressBar O_P_progress,P_S_progress,S_D_progress;
    private TextView yourrecentorderTitle;
    private TextView addressname,address,pincode;
    private Button signOutBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_account, container, false);

        ////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ////loading dialog

        profileView = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        layoutContainer = view.findViewById(R.id.layout_container);
        currentOrderImage = view.findViewById(R.id.current_order_image);
        tvCurrentOrderStatus = view.findViewById(R.id.tv_current_order_status);
        orderIndicator = view.findViewById(R.id.ordered_indicator);
        packedIndicator = view.findViewById(R.id.packed_indicator);
        shippendIndicator = view.findViewById(R.id.shipped_indicator);
        deliveredIndicator = view.findViewById(R.id.delivered_indicator);
        O_P_progress = view.findViewById(R.id.order_packed_progress);
        P_S_progress = view.findViewById(R.id.packed_shipped_progress);
        S_D_progress = view.findViewById(R.id.shipped_delivered_progress);
        yourrecentorderTitle = view.findViewById(R.id.your_recent_orders_titles);
        recentOrdersContainer = view.findViewById(R.id.recent_oreder_container);
        addressname = view.findViewById(R.id.address_fullname);
        address = view.findViewById(R.id.addresses);
        pincode = view.findViewById(R.id.address_pincode);
        signOutBtn = view.findViewById(R.id.sign_out_btn);
        settingsBtn = view.findViewById(R.id.settings_btn);


        layoutContainer.getChildAt(1).setVisibility(View.GONE);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                for (MyOrderItemModel orderItemModel : DBqueries.myOrderItemModelList) {
                    if (!orderItemModel.isCancellationRequested()) {
                        if (!orderItemModel.getOrderStatus().equals("Delivered") && !orderItemModel.getOrderStatus().equals("Cancelled")){
                            layoutContainer.getChildAt(1).setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(orderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.banner_placeholder)).into(currentOrderImage);
                        tvCurrentOrderStatus.setText(orderItemModel.getOrderStatus());

                        switch (orderItemModel.getOrderStatus()) {
                            case "Ordered":
                                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                break;
                            case "Packed":
                                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                O_P_progress.setProgress(100);
                                break;
                            case "Shipped":
                                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                shippendIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                O_P_progress.setProgress(100);
                                P_S_progress.setProgress(100);
                                break;
                            case "Out for Delivery":
                                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                shippendIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                O_P_progress.setProgress(100);
                                P_S_progress.setProgress(100);
                                S_D_progress.setProgress(100);
                                break;
                        }
                    }
                }
                }
                int i = 0;
                for (MyOrderItemModel myOrderItemModel : DBqueries.myOrderItemModelList) {
                    if (i < 4) {
                        if (myOrderItemModel.getOrderStatus().equals("Delivered")) {
                            Glide.with(getContext()).load(myOrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.banner_placeholder)).into((CircleImageView) recentOrdersContainer.getChildAt(i));
                            i++;
                        }
                    } else {
                        break;
                    }
                }
                if (i == 0) {
                    yourrecentorderTitle.setText("No recent Orders.");
                }

                if (i < 3) {
                    for (int x = i; x < 4; x++) {
                        recentOrdersContainer.getChildAt(x).setVisibility(View.GONE);
                    }
                }
//                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadingDialog.setOnDismissListener(null);
                        if (DBqueries.addressesModelList.size() == 0) {
                            addressname.setText("No address found.");
                            address.setText("-");
                            pincode.setText("-");
                        } else {
                            setAddress();
                        }
                    }
                });
                DBqueries.loadAddresses(getContext(), loadingDialog, false);

            }
        });
        DBqueries.loadOrders(getContext(),null,loadingDialog);

        Button viewAllAddressBtn = view.findViewById(R.id.view_all_addresses_btn);
        viewAllAddressBtn.setOnClickListener(v -> {
            Intent myAddressesIntent = new Intent(getContext(), MyAddressesActivity.class);
            myAddressesIntent.putExtra("MODE",MANAGE_ADDRESS);
            startActivity(myAddressesIntent);
        });

        signOutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            DBqueries.clearData();
            Intent registerIntent = new Intent(getContext(),Login.class);
            startActivity(registerIntent);
            getActivity().finish();
        });

        settingsBtn.setOnClickListener(v -> {
            Intent updateuserInfo = new Intent(getContext(), UpdateUserInfoActivity.class);
            updateuserInfo.putExtra("Name",name.getText());
            updateuserInfo.putExtra("Email",email.getText());
            updateuserInfo.putExtra("Photo",DBqueries.profile);
            startActivity(updateuserInfo);
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        name.setText(DBqueries.fullname);
        email.setText(DBqueries.email);
        if (!DBqueries.profile.equals("")){
            Glide.with(getContext()).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.baseline_person_24)).into(profileView);
        }else {
            profileView.setImageResource(R.drawable.baseline_person_24);
        }

        if (!loadingDialog.isShowing()){
            if (DBqueries.addressesModelList.size() == 0) {
                addressname.setText("No address found.");
                address.setText("-");
                pincode.setText("-");
            } else {
                setAddress();
            }
        }
    }

    private void setAddress() {
        String nametext, mobileNo;
        nametext = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        if (DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo().equals("")) {
            addressname.setText(nametext + " / " + mobileNo);
        }else {
            addressname.setText(nametext + " / " + mobileNo + " / " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo());
        }
        String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatNo();
        String locality = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getState();
        if (landmark.equals(" ")){
            address.setText(flatNo + ", " + locality + " , " + city + ", " + state);
        }else {
            address.setText(flatNo + ", " + locality + ", " + landmark + ", " + city + ", " + state);
        }
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPinCode());
    }
}