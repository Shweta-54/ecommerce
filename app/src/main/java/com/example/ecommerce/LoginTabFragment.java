package com.example.ecommerce;

import static com.example.ecommerce.Login.onResetPasswordFragment;

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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginTabFragment extends Fragment {


    public LoginTabFragment() {
        // Default empty constructor
    }

    float v=0;
    private ImageButton loginclosebtn;
    private TextView forgotpassword;
    private TextView dontHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email;
    private EditText password;
    private Button  loginBtn;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    public static boolean disableCloseBtn = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_tab_fragment, container, false);
        super.onViewCreated(view, savedInstanceState);



        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.pass);
        forgotpassword = view.findViewById(R.id.forgetpass);
        loginBtn = view.findViewById(R.id.btnLogin);
        dontHaveAnAccount = view.findViewById(R.id.donthaveanaccount);
        parentFrameLayout = requireActivity().findViewById(R.id.login_framelayout);
        progressBar = view.findViewById(R.id.loginpprogressbar);
        loginclosebtn = view.findViewById(R.id.loginclosebtn);
        email.setTranslationY(300);
        password.setTranslationY(300);
        forgotpassword.setTranslationY(300);
        loginBtn.setTranslationY(300);

        firebaseAuth = FirebaseAuth.getInstance();

        if (disableCloseBtn){
            loginclosebtn.setVisibility(View.GONE);
        }else{
            loginclosebtn.setVisibility(View.VISIBLE);
        }

        email.setAlpha(v);
        password.setAlpha(v);
        forgotpassword.setAlpha(v);
        loginBtn.setAlpha(v);

        email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgotpassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginBtn.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccount.setOnClickListener(v -> setFragment(new SignupTabFragment()));
        loginclosebtn.setOnClickListener(v -> mainIntent());

        forgotpassword.setOnClickListener(view1 -> {
            onResetPasswordFragment = true;
            setFragment(new ResetPasswordFragment());

        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                  checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loginBtn.setOnClickListener(view2 -> checkEmailAndPassword());
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void checkInputs(){
      if (!TextUtils.isEmpty(email.getText())) {
          if (!TextUtils.isEmpty(password.getText())) {
              loginBtn.setEnabled(true);
              loginBtn.setTextColor(Color.rgb(255,255,255));
          }else {
              loginBtn.setEnabled(false);
              loginBtn.setTextColor(Color.argb(50,255,255,255));
          }
      }else{
          loginBtn.setEnabled(false);
          loginBtn.setTextColor(Color.argb(50,255,255,255));
      }
    }

    private void checkEmailAndPassword(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if (email.getText().toString().matches(emailPattern)){
                 if (password.length() >=8) {
                     progressBar.setVisibility(View.VISIBLE);
                     loginBtn.setEnabled(true);
                     loginBtn.setTextColor(Color.argb(50,255,255,255));
                      firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                              .addOnCompleteListener(task -> {
                                  if (task.isSuccessful()) {
                                      mainIntent();
                                  }else{
                                      progressBar.setVisibility(View.INVISIBLE);
                                      loginBtn.setEnabled(true);
                                      loginBtn.setTextColor(Color.rgb(255,255,255));
                                      String error = Objects.requireNonNull(task.getException()).getMessage();
                                      Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                  }

                              });
                 }else {
                     Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                 }
        }else {
            Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void mainIntent(){
        if (disableCloseBtn){
            disableCloseBtn = false;
        }else {
            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);
        }
        requireActivity().finish();
    }
}
