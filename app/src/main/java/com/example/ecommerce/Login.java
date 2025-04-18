package com.example.ecommerce;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Login extends AppCompatActivity {

    private FrameLayout frameLayout;
    public static boolean onResetPasswordFragment = false;
    public static boolean setSignupFragment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

         frameLayout = findViewById(R.id.login_framelayout);

         if (setSignupFragment){
             setSignupFragment = false;
             setDefaultFragment(new SignupTabFragment());
         }else {
             setDefaultFragment(new LoginTabFragment());
         }
     
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            LoginTabFragment.disableCloseBtn = false;
            SignupTabFragment.disableCloseBtn = false;
            if (onResetPasswordFragment){
                onResetPasswordFragment = false;
                setFragment(new SignupTabFragment());
                return false;

            }

        }

        return super.onKeyDown(keyCode, event);
    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}