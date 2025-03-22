package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyCartFragment extends Fragment {

    private Dialog loadingDialog;
    public static CartAdapter  cartAdapter;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        ////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ////loading dialog

        RecyclerView cartItemsRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        Button continueBtn = view.findViewById(R.id.cart_continue_btn);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);

//        if (DBqueries.cartItemModelList.size() == 0){
//            DBqueries.cartList.clear();
//            DBqueries.loadCartList(getContext(),loadingDialog,true);
//        }else {
//            loadingDialog.dismiss();
//        }
//        cartAdapter = new CartAdapter(DBqueries.cartItemModelList);
//        cartItemsRecyclerView.setAdapter(cartAdapter);
//        cartAdapter.notifyDataSetChanged();


        continueBtn.setOnClickListener(v -> {
            Intent  deliverIntent = new Intent(getContext(), AddAddressActivity.class);
            requireContext().startActivity(deliverIntent);
        });
        return view;
    }
}///usha