package com.example.ecommerce;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class SignupTabFragment extends Fragment {

    public SignupTabFragment() {
        // Default empty constructor
    }

    private TextView alreadyHaveAnAccount;
    private EditText email;
    private EditText fullname;
    private  EditText password;
    private  EditText confirmPassword;
    private Button signup;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.signup_tab_fregment,container,false);

        alreadyHaveAnAccount = view.findViewById(R.id.alreadyhaveanaccount);
        email = view.findViewById(R.id.email);
        fullname = view.findViewById(R.id.fullname);
        password = view.findViewById(R.id.pass);
        confirmPassword = view.findViewById(R.id.confirmpass);
        signup = view.findViewById(R.id.signupbtn);
        progressBar = view.findViewById(R.id.signupprogressbar);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }
}
