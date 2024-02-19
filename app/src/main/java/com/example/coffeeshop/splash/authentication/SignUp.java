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

import com.example.coffeeshop.splash.internetAccess.NetworkChangeListener;
import com.example.coffeeshop.splash.main.MainActivity;
import com.example.coffeeshop.splash.modal.User;
import com.example.coffeeshop.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class SignUp extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    LinearLayout main, left, main1;
    TextView login;
    private String suffix;
    private String prefix;
    private ProgressBar progressBar;

    private TextInputLayout nameTextInputLayout;
    private TextInputLayout phoneTextInputLayout;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private Button signUpButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Example of initialization
         progressBar = findViewById(R.id.progress); // Replace with your ProgressBar ID




        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        // Initialize TextInputLayouts
        nameTextInputLayout = findViewById(R.id.name);
        phoneTextInputLayout = findViewById(R.id.phone);
        emailTextInputLayout = findViewById(R.id.email);
        passwordTextInputLayout = findViewById(R.id.pass);
        // Get the suffix text from resources
        suffix = getString(R.string.suffix);
        prefix = getString(R.string.prefix);

        // Initialize Button
        signUpButton = findViewById(R.id.btn);

        main = findViewById(R.id.main_logo);
        left = findViewById(R.id.animation1);
        main1 = findViewById(R.id.main1);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
        main.startAnimation(slideUpAnimation);

        Animation slideLeftAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        left.startAnimation(slideLeftAnimation);

        Animation slideLeftAnimation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
        main1.startAnimation(slideLeftAnimation1);
    }


    private void signUp() {
        String name = nameTextInputLayout.getEditText().getText().toString().trim();
        String phone1 = phoneTextInputLayout.getEditText().getText().toString().trim();
        String phone = phone1 + prefix;
        String email1 = emailTextInputLayout.getEditText().getText().toString().trim();
        String email = email1 + suffix;
        String password = passwordTextInputLayout.getEditText().getText().toString().trim();

        // Check if phone number exceeds the limit
        int phoneLimit = 10;
        if (phone1.length() > phoneLimit) {
            Toasty.warning(SignUp.this, "Phone No should Exceed the limit " + phoneLimit + " characters", Toast.LENGTH_SHORT, true).show();
            return;
        }

        // Check if password exceeds the limit
        int passwordLimit = 6;
        if (password.length() > passwordLimit) {
            Toasty.warning(SignUp.this, "Password should Exceed the limit " + passwordLimit + " characters", Toast.LENGTH_SHORT, true).show();

            return;
        }

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toasty.warning(SignUp.this, "All Fields Are Required.", Toast.LENGTH_SHORT, true).show();
            return;
        }
        // Before starting signup, show the ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUserData(user.getUid(), name, phone, email, password);
                        openNewActivity(name);
                        Toasty.success(this, "Account Created Successfully!!!", Toast.LENGTH_SHORT, true).show();
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException | FirebaseAuthInvalidCredentialsException e) {
                            Toasty.error(this, "Invalid email or password.", Toast.LENGTH_SHORT, true).show();
                        } catch (Exception e) {
                            Toasty.error(this, "Already Exist!! Try Login.", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void saveUserData(String userId, String name, String phone, String email, String password) {
        User user = new User(name, phone, email, password);
        mDatabase.child(userId).setValue(user);
    }

    private void openNewActivity(String name) {
        // Save the name in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NAME", name);
        editor.apply();

        Intent intent = new Intent(SignUp.this, MainActivity.class);
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
