package com.example.ecommerce;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ProductSpecificationFragment extends Fragment {


    private RecyclerView productspecificationrecyclerview;

    public ProductSpecificationFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_specification, container, false);

        productspecificationrecyclerview = view.findViewById(R.id.product_specification_recyclerview);

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        productspecificationrecyclerview.setLayoutManager(linearLayoutManager);

        List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
        productSpecificationModelList.add(new ProductSpecificationModel(0,"General"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(0,"Display"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(0,"General"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(0,"Display"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));
        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","4GB"));



        ProductSpecificationAdapter productSpecificationAdapter = new ProductSpecificationAdapter(productSpecificationModelList);
        productspecificationrecyclerview.setAdapter(productSpecificationAdapter);
        productSpecificationAdapter.notifyDataSetChanged();

        return view;
    }
}