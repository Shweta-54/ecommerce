package com.example.ecommerce;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;

public class LoginTabFragment extends Fragment {

    float v=0;
    private TextView forgotpassword;
    public LoginTabFragment() {
        // Default empty constructor
    }

    private TextView dontHaveAnAccount;
    private TextView forgotPassword;
    private FrameLayout parentFrameLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_tab_fragment, container, false);
        super.onViewCreated(view, savedInstanceState);



        EditText email = view.findViewById(R.id.email);
        EditText pass = view.findViewById(R.id.pass);
        TextView forgetpass = view.findViewById(R.id.forgetpass);
        Button login = view.findViewById(R.id.btnLogin);
        dontHaveAnAccount = view.findViewById(R.id.donthaveanaccount);
        parentFrameLayout = getActivity().findViewById(R.id.login_framelayout);




        email.setTranslationY(300);
        pass.setTranslationY(300);
        forgetpass.setTranslationY(300);
        login.setTranslationY(300);

        email.setAlpha(v);
        pass.setAlpha(v);
        forgetpass.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgetpass.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignupTabFragment());

                
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ResetPasswordFragment());

            }

            private void setFragment(ResetPasswordFragment resetPasswordFragment) {
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();

    }
}
