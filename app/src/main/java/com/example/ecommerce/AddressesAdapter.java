package com.example.ecommerce;

import static com.example.ecommerce.DeliveryActivity.SELECT_ADDRESS;
import static com.example.ecommerce.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.ecommerce.MyAddressesActivity.refreshItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int preSelectedPosition;

    public AddressesAdapter(List<AddressesModel> addressesModelList,int MODE) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
    }

    @NonNull
    @Override
    public AddressesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesAdapter.Viewholder viewholder, int position) {
        String name = addressesModelList.get(position).getFullname();
        String  address = addressesModelList.get(position).getAddress();
        String pincode = addressesModelList.get(position).getPincode();
        Boolean selected = addressesModelList.get(position).getSelected();
        viewholder.setData(name,address,pincode,selected,position);

    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView fullname;
        private TextView address;
        private TextView pincode;
        private ImageView icon;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            fullname = itemView.findViewById(R.id.name1);
            address = itemView.findViewById(R.id.address1);
            pincode = itemView.findViewById(R.id.pincode1);
            icon = itemView.findViewById(R.id.icon_view);
        }
        private void setData(String username,String userAddress,String userPincode,Boolean selected,int position){
            fullname.setText(username);
            address.setText(userAddress);
            pincode.setText(userPincode);

            if (MODE == SELECT_ADDRESS){
                icon.setImageResource(R.drawable.baseline_check_24);
                if (selected){
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                }else {
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (preSelectedPosition != position) {
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(preSelectedPosition).setSelected(false);
                            refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                        }
                    }
                });

            } else if (MODE == MANAGE_ADDRESS) {


            }

        }
    }
}
