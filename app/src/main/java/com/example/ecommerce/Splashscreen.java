package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class Splashscreen extends AppCompatActivity {
    private static final int Splash_screen = 5000;
    //Variables
    Animation topanim,bottomanim;
    ImageView logo;
    TextView logoname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splashscreen);

        //Animation
        topanim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        //call the imageview and textview
        logo = findViewById(R.id.splashimage);
        logoname = findViewById(R.id.splashtextView);
        logo.setAnimation(topanim);
        logoname.setAnimation(bottomanim);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(Splashscreen.this, MainActivity.class));
            ActivityUtils.applyTransition(Splashscreen.this);
            finish();
        }, 2000); // just for checking
    }
}