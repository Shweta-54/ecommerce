package com.example.ecommerce;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupTabFragment extends Fragment {

    public SignupTabFragment() {
        // Default empty constructor
    }

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email;
    private EditText fullname;
    private  EditText password;
    private  EditText confirmPassword;
    private Button signupbtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.signup_tab_fregment,container,false);

        alreadyHaveAnAccount = view.findViewById(R.id.alreadyhaveanaccount);
        parentFrameLayout = getActivity().findViewById(R.id.login_framelayout);
        email = view.findViewById(R.id.email);
        fullname = view.findViewById(R.id.fullname);
        password = view.findViewById(R.id.pass);
        confirmPassword = view.findViewById(R.id.confirmpass);
        signupbtn = view.findViewById(R.id.signupbtn);
        progressBar = view.findViewById(R.id.signupprogressbar);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }
    //heelo world
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new LoginTabFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkEmailAndPassword();
            }
        });
    }




    //hello world this is my code
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())){
            if (!TextUtils.isEmpty(fullname.getText())){
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8){
                    if (!TextUtils.isEmpty(confirmPassword.getText())){
                             signupbtn.setEnabled(true);
                             signupbtn.setTextColor(Color.rgb(255,255,255));
                    }else {
                        signupbtn.setEnabled(false);
                        signupbtn.setTextColor(Color.argb(50,255,255,255));
                    }
                }else {
                    signupbtn.setEnabled(false);
                    signupbtn.setTextColor(Color.argb(50,255,255,255));
                }
            }else {
                signupbtn.setEnabled(false);
                signupbtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else {
            signupbtn.setEnabled(false);
            signupbtn.setTextColor(Color.argb(50,255,255,255));

        }
    }
    private void checkEmailAndPassword() {
        if (email.getText().toString().matches(emailPattern)){
            if (password.getText().toString().equals(confirmPassword.getText().toString())){

                progressBar.setVisibility(View.VISIBLE);
                signupbtn.setEnabled(false);
                signupbtn.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Intent mainIntent = new Intent(getActivity(),MainActivity.class);
                                    startActivity(mainIntent);
                                    getActivity().finish();
                                }else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signupbtn.setEnabled(true);
                                    signupbtn.setTextColor(Color.rgb(255,255,255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else {
                confirmPassword.setError("Password doesn't matched!");
            }
        }else {
            email.setError("Invalid Email!");

        }
    }
}
