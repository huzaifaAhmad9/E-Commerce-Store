package com.example.coffeeshop.splash.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshop.R;
import com.example.coffeeshop.splash.internetAccess.NetworkChangeListener;
import com.example.coffeeshop.splash.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    LinearLayout main, left;
    private ProgressBar progressBar;
    TextView signup;
    String suffix;
    private Button loginButton;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progress);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, open MainActivity
            openMainActivity(currentUser);
            return; // Finish the current activity
        }

        loginButton = findViewById(R.id.btn);
        suffix = getString(R.string.suffix);
        main = findViewById(R.id.main_logo);
        left = findViewById(R.id.main1);
        emailTextInputLayout = findViewById(R.id.email);
        passwordTextInputLayout = findViewById(R.id.pass);
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
                finish();
            }
        });

        // login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate the input
                String email1 = emailTextInputLayout.getEditText().getText().toString().trim();
                String email = email1 + suffix;  // Corrected line
                String password = passwordTextInputLayout.getEditText().getText().toString().trim();

                // Check if password length is greater than 6
                int passwordLimit = 6;
                if (password.length() > passwordLimit) {
                    Toasty.warning(Login.this, "Password should Exceed the limit " + passwordLimit + " characters", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                if (email.isEmpty() || password.isEmpty()) {
                    Toasty.warning(Login.this, "Please enter both email and password", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                // Authenticate with Firebase
                signInWithEmailAndPassword(email, password);
            }
        });

        // Load the animation
        Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
        // Start the animation on the button
        main.startAnimation(slideUpAnimation);

        Animation slideLeftAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
        // Start the animation on the button
        left.startAnimation(slideLeftAnimation);
    }

    // extra functions

    private void signInWithEmailAndPassword(String email, String password) {
        // Before starting login, show the ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        // After login is complete, hide the ProgressBar
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            openMainActivity(user);
                            Toasty.success(Login.this, "Login Successfully!", Toast.LENGTH_SHORT, true).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toasty.error(Login.this, "Authentication failed. Check your email and password.", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void openMainActivity(FirebaseUser user) {
        // Retrieve the name from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String name = preferences.getString("NAME", "");
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.putExtra("NAME", name);
        startActivity(intent);
        finish();
    }




    // for network checking
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
