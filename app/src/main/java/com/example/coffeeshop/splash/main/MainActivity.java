package com.example.coffeeshop.splash.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.coffeeshop.R;
import com.example.coffeeshop.splash.authentication.Login;
import com.example.coffeeshop.splash.fragment.AboutUsFragment;
import com.example.coffeeshop.splash.fragment.CartFragment;
import com.example.coffeeshop.splash.fragment.HomeFragment;
import com.example.coffeeshop.splash.internetAccess.NetworkChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String name = preferences.getString("NAME", "Default Name");

        TextView welcomeText = findViewById(R.id.userName);
        welcomeText.setText(name + "!!");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_item1) {
                loadFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.menu_item2) {
                loadFragment(new CartFragment());
                return true;
            } else if (itemId == R.id.menu_item4) {
                loadFragment(new AboutUsFragment());
                return true;
            } else {
                return false;
            }
        });

        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void navigateToLoginScreen() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            navigateToLoginScreen();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // Check if the current fragment is AboutUsFragment or CartFragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof AboutUsFragment || currentFragment instanceof CartFragment) {
            // If yes, load the HomeFragment
            loadFragment(new HomeFragment());

            // Update the selected item in the BottomNavigationView
            bottomNavigationView.setSelectedItemId(R.id.menu_item1);
        } else {
            // Otherwise, perform the default back action
            super.onBackPressed();
        }
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
