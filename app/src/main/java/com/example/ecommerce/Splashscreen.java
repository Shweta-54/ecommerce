package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class Splashscreen extends AppCompatActivity {
    //Variables
    Animation topanim,bottomanim;
    ImageView logo;
    TextView logoname;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splashscreen);



        firebaseAuth = FirebaseAuth.getInstance();
        //Animation
        topanim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        //call the imageview and textview
        logo = findViewById(R.id.splashimage);
        logoname = findViewById(R.id.splashtextView);
        logo.setAnimation(topanim);
        logoname.setAnimation(bottomanim);


            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

            if (currentUser == null) {
               Intent intent = new Intent(Splashscreen.this, Login.class);
                startActivity(intent);
                finish();
            } else {
                FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid()).update("Last_seen", FieldValue.serverTimestamp())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                   Intent mainintent = new Intent(Splashscreen.this, MainActivity.class);
                                    startActivity(mainintent);
                                    finish();
                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(Splashscreen.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }

    }

}