package com.example.coffeeshop.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeshop.R;
import com.example.coffeeshop.splash.authentication.Login;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Splash_Screen extends AppCompatActivity {
    Button btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        btn = findViewById(R.id.btn);

        // Load the animation
        Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        // Start the animation on the button
        btn.startAnimation(slideUpAnimation);

        // Moving to new Activity
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check and enable GPS
                checkAndTurnOnGPS();
            }
        });
    }

    private void checkAndTurnOnGPS() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(this)
                        .checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // GPS is already turned on
                    Toast.makeText(Splash_Screen.this, "Fetching Your Location!!", Toast.LENGTH_SHORT).show();
                    moveToLoginActivity();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(Splash_Screen.this, 1);
                            } catch (IntentSender.SendIntentException | ClassCastException e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // GPS settings are not available on the device
                            moveToLoginActivity(); // Proceed to Login activity even if GPS is not enabled
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // User enabled GPS or GPS was already enabled
            Toast.makeText(this, "Location is turned On", Toast.LENGTH_SHORT).show();
            moveToLoginActivity();
        }
    }

    private void moveToLoginActivity() {
        Intent i = new Intent(Splash_Screen.this, Login.class);
        startActivity(i);
        finish();
    }
}
