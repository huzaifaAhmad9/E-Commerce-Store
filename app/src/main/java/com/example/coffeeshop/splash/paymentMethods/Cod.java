package com.example.coffeeshop.splash.paymentMethods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeshop.R;
import com.example.coffeeshop.splash.main.MainActivity;
import com.example.coffeeshop.splash.ratingBar.RateDialog;

public class Cod extends AppCompatActivity  {
    LottieAnimationView animation;
    Button btn,btn1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod);
        btn = findViewById(R.id.btn);
        btn1 = findViewById(R.id.btn1);

        // Load the animation
        Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        // Start the animation on the button
        btn.startAnimation(slideUpAnimation);
        // Moving to new Activity
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateDialog rateDialog = new RateDialog(Cod.this);
                rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                rateDialog.setCancelable(false);
                rateDialog.show();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cod.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });



        // lottie animation
        animation = findViewById(R.id.animation);
        animation.setAnimation(R.raw.thnks);
        animation.playAnimation();



    }


}