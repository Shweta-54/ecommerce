package com.example.ecommerce;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddAddressActivity extends AppCompatActivity {

    private EditText city;
    private EditText locality;
    private EditText flatNo;
    private EditText pinCode;
    private EditText landmark;
    private EditText name;
    private EditText mobileNo;
    private EditText alternateMobileNo;
    private Spinner stateSpinner;
    private String [] statelist;
    private String selectedState;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ////loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ////loading dialog

         statelist = getResources().getStringArray(R.array.india_states);
        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flatNo = findViewById(R.id.flatno);
        pinCode = findViewById(R.id.pincode);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.moblie_no);
        alternateMobileNo = findViewById(R.id.alternate_moblie_no);
        stateSpinner = findViewById(R.id.state_spinner);
        Button saveBtn = findViewById(R.id.save_btn);


        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,statelist);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(spinnerAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = statelist[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saveBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(city.getText())) {
                if (!TextUtils.isEmpty(locality.getText())) {
                    if (!TextUtils.isEmpty(flatNo.getText())) {
                        if (!TextUtils.isEmpty(pinCode.getText()) && pinCode.getText().length() == 6) {
                            if (!TextUtils.isEmpty(name.getText())) {
                                if (!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length() == 10) {
                                    loadingDialog.show();
                                    String fullAddress = flatNo.getText().toString() + ", " + locality.getText().toString() + ", " + landmark.getText().toString() + ", " + city.getText().toString() + ", " + selectedState + ", ";
                                    Map<String,Object> addAddress = new HashMap();
                                    addAddress.put("list_size",(long) DBqueries.addressesModelList.size()+1);
                                    if (TextUtils.isEmpty(alternateMobileNo.getText())) {
                                    addAddress.put("fullname_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),name.getText().toString() + "-" + mobileNo.getText().toString());
                                    }else {
                                        addAddress.put("fullname_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),name.getText().toString() + "-" + mobileNo.getText().toString() + " / " + alternateMobileNo.getText().toString());

                                    }
                                    addAddress.put("address_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),fullAddress);
                                    addAddress.put("pincode_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),pinCode.getText().toString());
                                    addAddress.put("selected_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),true);
                                    if (DBqueries.addressesModelList.size() > 0) {
                                        addAddress.put("selected_" + (DBqueries.selectedAddress + 1), false);
                                    }

                                    FirebaseFirestore.getInstance().collection("USERS")
                                            .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                            .document("MY_ADDRESSES")
                                            .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (DBqueries.addressesModelList.size() > 0){
                                                            DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                        }
                                                        if (TextUtils.isEmpty(alternateMobileNo.getText())) {
                                                        DBqueries.addressesModelList.add(new AddressesModel(name.getText().toString() + "-" + mobileNo.getText().toString(),fullAddress,pinCode.getText().toString(),true));
                                                        }else {
                                                            DBqueries.addressesModelList.add(new AddressesModel(name.getText().toString() + "-" + mobileNo.getText().toString() + " / " + alternateMobileNo.getText().toString(),fullAddress,pinCode.getText().toString(),true));
                                                        }
                                                        DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;
                                                        if (getIntent().getStringExtra("INTENT").equals("deliverIntent")) {
                                                            Intent deliverIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                            startActivity(deliverIntent);
                                                        }
                                                            finish();
                                                }else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    loadingDialog.dismiss();
                                                }
                                            });
                                }else {
                                    mobileNo.requestFocus();
                                    Toast.makeText(this, "Please Provide a valid No. ", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                name.requestFocus();
                            }
                        }else {
                            pinCode.requestFocus();
                            Toast.makeText(this, "Please Provide a valid Pincode ", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        flatNo.requestFocus();
                    }
                }else {
                    locality.requestFocus();
                }
            }else {
                city.requestFocus();
            }


        });
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}