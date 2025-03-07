package com.example.ecommerce;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageButton;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupTabFragment extends Fragment {

    public SignupTabFragment() {
        // Default empty constructor
    }

    private ImageButton signupclosebtn;
    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email;
    private EditText fullname;
    private  EditText password;
    private  EditText confirmPassword;
    private Button signupbtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    float v=0;



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
        signupclosebtn = view.findViewById(R.id.signupclosebtn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        email.setTranslationY(300);
        fullname.setTranslationY(300);
        password.setTranslationY(300);
        confirmPassword.setTranslationY(300);
        signupbtn.setTranslationY(300);
        alreadyHaveAnAccount.setTranslationY(300);

        firebaseAuth = FirebaseAuth.getInstance();

        email.setAlpha(v);
        fullname.setAlpha(v);
        password.setAlpha(v);
        confirmPassword.setAlpha(v);
        signupbtn.setAlpha(v);
        alreadyHaveAnAccount.setAlpha(v);

        email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        fullname.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        password.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        confirmPassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        signupbtn.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        alreadyHaveAnAccount.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new LoginTabFragment());
            }
        });

        signupclosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
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

        Drawable customeErrorIcon = getResources().getDrawable(R.drawable.alerticon);
        customeErrorIcon.setBounds(0,0,customeErrorIcon.getIntrinsicWidth(),customeErrorIcon.getIntrinsicHeight());

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

                                    Map< Object,String > userdata = new HashMap<>();
                                    userdata.put("fullname",fullname.getText().toString());

                                    firebaseFirestore.collection("USERS")
                                            .add(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()){
                                                        mainIntent();
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
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signupbtn.setEnabled(true);
                                    signupbtn.setTextColor(Color.rgb(255,255,255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else {
                confirmPassword.setError("Password doesn't matched!",customeErrorIcon);
            }
        }else {
            email.setError("Invalid Email!",customeErrorIcon);

        }
    }

    private void mainIntent(){
        Intent mainIntent = new Intent(getActivity(),MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }
}
