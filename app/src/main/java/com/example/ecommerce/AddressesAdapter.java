package com.example.ecommerce;

import static com.example.ecommerce.DeliveryActivity.SELECT_ADDRESS;
import static com.example.ecommerce.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.ecommerce.MyAddressesActivity.refreshItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private final List<AddressesModel> addressesModelList;
    private final int MODE;
    private int preSelectedPosition;

    public AddressesAdapter(List<AddressesModel> addressesModelList,int MODE) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
        preSelectedPosition = DBqueries.selectedAddress;
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
        String mobileNo = addressesModelList.get(position).getMobileNo();
        String  address = addressesModelList.get(position).getAddress();
        String pincode = addressesModelList.get(position).getPincode();
        Boolean selected = addressesModelList.get(position).getSelected();

        viewholder.setData(name,address,pincode,selected,position,mobileNo);

    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private final TextView fullname;
        private final TextView address;
        private final TextView pincode;
        private final ImageView icon;
        private final LinearLayout optionContainer;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            fullname = itemView.findViewById(R.id.name1);
            address = itemView.findViewById(R.id.address1);
            pincode = itemView.findViewById(R.id.pincode1);
            icon = itemView.findViewById(R.id.icon_view);
            optionContainer = itemView.findViewById(R.id.option_container);
        }
        private void setData(String username,String userAddress,String userPincode,Boolean selected,int position,String mobileNo) {
            fullname.setText(username+" - "+mobileNo);
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
                itemView.setOnClickListener(v -> {
                    if (preSelectedPosition != position) {
                        addressesModelList.get(position).setSelected(true);
                        addressesModelList.get(preSelectedPosition).setSelected(false);
                        refreshItem(preSelectedPosition, position);
                        preSelectedPosition = position;
                        DBqueries.selectedAddress = position;
                    }
                });

            } else if (MODE == MANAGE_ADDRESS) {
                optionContainer.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.baseline_more_vert_24);
                icon.setOnClickListener(v -> {
                    optionContainer.setVisibility(View.VISIBLE);
                    refreshItem(preSelectedPosition,preSelectedPosition);
                    preSelectedPosition = position;
                });
                itemView.setOnClickListener(v -> {
                    refreshItem(preSelectedPosition,preSelectedPosition);
                    preSelectedPosition = -1;
                });
            }

        }
    }
}
