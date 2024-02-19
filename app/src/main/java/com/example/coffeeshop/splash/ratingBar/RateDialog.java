package com.example.coffeeshop.splash.ratingBar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.coffeeshop.R;

public class RateDialog extends Dialog {
    AppCompatButton later,rateNow;
    RatingBar ratingBar;
    ImageView ratingImage;
    private float userRate = 0;
    public RateDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_dialog_layout);
        later = findViewById(R.id.later);
        rateNow = findViewById(R.id.rateNow);
        ratingImage = findViewById(R.id.ratingImage);
        ratingBar = findViewById(R.id.ratingBar);
        rateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Thanks For Rating!!!", Toast.LENGTH_SHORT).show();
                dismiss();

            }
        });
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide rating dialog
                dismiss();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating <= 1){
                    ratingImage.setImageResource(R.drawable.one_star);
                } else if (rating <= 2) {
                    ratingImage.setImageResource(R.drawable.two_star);
                } else if (rating <= 3) {
                    ratingImage.setImageResource(R.drawable.three_star);
                } else if (rating <= 4) {
                    ratingImage.setImageResource(R.drawable.four_star);
                } else if (rating <= 5) {
                    ratingImage.setImageResource(R.drawable.five_star);
                }
                // animate emoji Image
                animateImage(ratingImage);

                // select rating by user
                userRate = rating;
            }
        });
    }
    private void animateImage(ImageView ratingImage){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1f, 0,1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }
}
