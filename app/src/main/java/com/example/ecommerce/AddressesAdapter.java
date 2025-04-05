package com.example.ecommerce;

import static com.example.ecommerce.DeliveryActivity.SELECT_ADDRESS;
import static com.example.ecommerce.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.ecommerce.MyAddressesActivity.refreshItem;

import android.content.Intent;
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
    private boolean refresh = false;

    public AddressesAdapter(List<AddressesModel> addressesModelList, int MODE) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
        preSelectedPosition = DBqueries.selectedAddress;
    }

    @NonNull
    @Override
    public AddressesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesAdapter.Viewholder viewholder, int position) {
        String city = addressesModelList.get(position).getCity();
        String locality = addressesModelList.get(position).getLocality();
        String flatNo = addressesModelList.get(position).getFlatNo();
        String pinCode = addressesModelList.get(position).getPinCode();
        String landmark = addressesModelList.get(position).getLandmark();
        String name = addressesModelList.get(position).getName();
        String mobileNo = addressesModelList.get(position).getMobileNo();
        String alternateMobileNo = addressesModelList.get(position).getAlternateMobileNo();
        String state = addressesModelList.get(position).getState();
        boolean selected = addressesModelList.get(position).getSelected();

        viewholder.setData(name, city, pinCode, selected, position, mobileNo, alternateMobileNo, flatNo, locality, state, landmark);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

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

        private void setData(String username, String city, String userPincode, Boolean selected, int position, String mobileNo, String alternateMobileNo, String flatNo, String locality, String state, String landmark) {
            {

                if (alternateMobileNo.equals("")) {
                    fullname.setText(username + " / " + mobileNo);
                } else {
                    fullname.setText(username + " / " + mobileNo + " / " + alternateMobileNo);
                }

                if (landmark.equals(" ")) {
                    address.setText(flatNo + ", " + locality + " , " + city + ", " + state);

                } else {
                    address.setText(flatNo + ", " + locality + ", " + landmark + ", " + city + ", " + state);

                }
                pincode.setText(userPincode);

                if (MODE == SELECT_ADDRESS) {
                    icon.setImageResource(R.drawable.baseline_check_24);
                    if (selected) {
                        icon.setVisibility(View.VISIBLE);
                        preSelectedPosition = position;
                    } else {
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
                    optionContainer.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//////edit address
                            Intent addNewAddressIntent = new Intent(itemView.getContext(), AddAddressActivity.class);
                            addNewAddressIntent.putExtra("INTENT", "update_address");
                            addNewAddressIntent.putExtra("index", position);
                            itemView.getContext().startActivity(addNewAddressIntent);

                        }
                    });
                    optionContainer.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { /////// remove address

                        }
                    });
                    icon.setImageResource(R.drawable.baseline_more_vert_24);
                    icon.setOnClickListener(v -> {
                        optionContainer.setVisibility(View.VISIBLE);
                        if (refresh) {
                            refreshItem(preSelectedPosition, preSelectedPosition);
                        } else {
                            refresh = true;
                        }
                        preSelectedPosition = position;
                    });

                    itemView.setOnClickListener(v -> {
                        refreshItem(preSelectedPosition, preSelectedPosition);
                        preSelectedPosition = -1;
                    });
                }

            }
        }
    }
}
