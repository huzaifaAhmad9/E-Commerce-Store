package com.example.coffeeshop.splash.checkout;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.coffeeshop.R;
import com.example.coffeeshop.splash.internetAccess.NetworkChangeListener;
import com.example.coffeeshop.splash.modal.CartManager;
import com.example.coffeeshop.splash.modal.CheckoutDetails;
import com.example.coffeeshop.splash.paymentMethods.Payment_Detail;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Checkout_Methods extends AppCompatActivity implements OnMapReadyCallback {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private TextInputLayout name, phone, location;
    private TextInputEditText editText;
    private LocationRequest locationRequest;
    private GoogleMap mMap;
    private Marker currentMarker;
    Button btn;
    private double DEFAULT_LATITUDE;
    private double DEFAULT_LONGITUDE;
    private static final float DEFAULT_ZOOM = 15.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_methods);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);
        editText = findViewById(R.id.editText);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getEditText().getText().toString().trim();
                String phoneText = phone.getEditText().getText().toString().trim();
                String locationText = editText.getText().toString().trim();
                // Check if any field is empty
                if (nameText.isEmpty() || phoneText.isEmpty() || locationText.isEmpty()) {
                    Toast.makeText(Checkout_Methods.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if the phone number is valid
                if (phoneText.length() != 10) {
                    Toast.makeText(Checkout_Methods.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Get the UID of the currently signed-in user
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Create a reference to the "checkout_details" node under the user's UID
                DatabaseReference checkoutRef = FirebaseDatabase.getInstance().getReference().child("checkout_details").child(uid);
                // Create a CheckoutDetails object or use a HashMap to store the data
                // For simplicity, let's assume a CheckoutDetails class with name, phone, and location properties
                CheckoutDetails checkoutDetails = new CheckoutDetails(nameText, phoneText, locationText);
                // Save checkout details to Firebase
                checkoutRef.setValue(checkoutDetails);
                CartManager.getInstance(Checkout_Methods.this).clearCart();
                // Display a success message
                Toast.makeText(Checkout_Methods.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                // opening new activity
                Intent i = new Intent(Checkout_Methods.this, Payment_Detail.class);
                startActivity(i);
                finish();
                // Clear text fields
                name.getEditText().setText("");
                phone.getEditText().setText("");
                editText.setText("");
            }
        });


        // Configuration for location updates, specifying high accuracy and intervals.
        /*
         The code you provided is configuring a LocationRequest object, which is part of the Google Play services Location API.
         locationRequest = LocationRequest.create();
         This line creates a new instance of LocationRequest.
         LocationRequest is a class that defines the quality of service parameters for location updates.
         locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
         This line sets the priority for the location request. In this case, it is set to PRIORITY_HIGH_ACCURACY,
         which means the app is requesting the most accurate location possible. This typically involves using GPS, Wi-Fi, and mobile networks.
         locationRequest.setInterval(5000);
         This line sets the interval between active location updates to 5000 milliseconds (5 seconds).
         It determines how often your app should receive location updates.
         In this case, the app is configured to receive updates every 5 seconds.
         locationRequest.setFastestInterval(2000);
         This line sets the fastest rate at which your app can handle location updates.
         Even if the specified interval is 5 seconds, the app will not receive updates faster than every 2 seconds.
         This is useful to conserve battery life and bandwidth.
         */
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        // Set the default location to the current device location
        setDefaultLocationToCurrent();

        // Automatically get and display the current location in the location field
        getCurrentLocation();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng defaultLocation = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        currentMarker = mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                editText.setText(getAddressFromLatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                currentMarker.setPosition(latLng);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                editText.setText(getAddressFromLatLng(latLng.latitude, latLng.longitude));
            }
        });
    }

    private void setDefaultLocationToCurrent() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //  Used for accessing the device's last known location and requesting location updates.
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Set the default location to the current device location
                            DEFAULT_LATITUDE = location.getLatitude();
                            DEFAULT_LONGITUDE = location.getLongitude();

                            // Update the map marker and camera
                            LatLng currentLocation = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
                            currentMarker.setPosition(currentLocation);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));

                            // Get and display the address in the location field
                            String locationName = getAddressFromLatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
                            editText.setText(locationName);
                        }
                    }
                });
    }

    private String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                fusedLocationProviderClient.removeLocationUpdates(this);

                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int index = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(index).getLatitude();
                    double longitude = locationResult.getLocations().get(index).getLongitude();

                    // Update the map marker and camera
                    LatLng currentLocation = new LatLng(latitude, longitude);
                    currentMarker.setPosition(currentLocation);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));

                    // Get and display the address in the location field
                    String locationName = getAddressFromLatLng(latitude, longitude);
                    editText.setText(locationName);
                }
            }
        }, Looper.getMainLooper());
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(this, new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(Checkout_Methods.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(Checkout_Methods.this, 1);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
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






