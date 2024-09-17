package com.example.mentormate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    View nContentview;
    Handler handler;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.splash);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        nContentview = findViewById(R.id.splash);
        nContentview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseUser != null )
                {
                    if(firebaseUser.isEmailVerified())
                    {
                        startActivity(new Intent(SplashActivity.this,HomePageActivity.class));
                    }else {
                        startActivity(new Intent(SplashActivity.this,SignupActivity.class));
                    }
                }else {
                    startActivity(new Intent(SplashActivity.this,SignupActivity.class));
                }
                finish();
            }
        },3000);
    }
}