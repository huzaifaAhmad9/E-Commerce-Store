package com.example.coffeeshop.splash.coffeeDetailsActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeshop.R;
import com.example.coffeeshop.splash.modal.CartItem;
import com.example.coffeeshop.splash.modal.CartManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Mo_Hazelnut extends AppCompatActivity {

    private ImageView favoriteImageView;
    private LinearLayout detailsLayout;

    private boolean isFavorite = false;

    // Firebase
    private FirebaseAuth auth;
    private DatabaseReference favoritesReference;
    private FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_hazelnut);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        favoritesReference = FirebaseDatabase.getInstance().getReference().child("favorites").child(currentUser.getUid());

        detailsLayout = findViewById(R.id.detailsLayout);
        favoriteImageView = findViewById(R.id.favoriteImageView);

        favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavoriteState();
            }
        });

        // Fetch initial state from the database
        fetchFavoriteState();
    }

    private void toggleFavoriteState() {
        isFavorite = !isFavorite;

        // Change image source based on the state
        int imageResource = isFavorite ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24;
        favoriteImageView.setImageResource(imageResource);

        // Show toast message based on the state
        String toastMessage = isFavorite ? "Item added to favorites" : "Item removed from favorites";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

        // Update the state in the database
        updateFavoriteState();
    }

    private void updateFavoriteState() {
        favoritesReference.child("MochaHazelnut").setValue(isFavorite);
    }

    private void fetchFavoriteState() {
        favoritesReference.child("MochaHazelnut").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Update the state based on the value in the database
                Boolean value = dataSnapshot.getValue(Boolean.class);
                isFavorite = (value != null && value);
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handling errors
            }

        });
    }

    private void updateUI() {
        // Update the UI based on the current state
        int imageResource = isFavorite ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24;
        favoriteImageView.setImageResource(imageResource);
    }

    private void addToCart() {
        // Get data from your views
        String itemName = ((TextView) findViewById(R.id.name)).getText().toString();
        String itemType = ((TextView) findViewById(R.id.type)).getText().toString();
        double itemPrice = Double.parseDouble(((TextView) findViewById(R.id.price)).getText().toString());
        int imageResource = R.drawable.moc2;  // Set your image resource here

        // Create a CartItem
        CartItem cartItem = new CartItem(itemName, itemType, itemPrice, 1, imageResource);

        // Add the item to the cart
        CartManager.getInstance(this).addItemToCart(cartItem);

        // Add the item to Firebase
        addToFirebase(cartItem);

        // Display a toast or perform any other action to indicate successful addition to the cart
        Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show();
    }

    // Add this method to be called when the "Add to Cart" button is clicked
    public void onAddToCartClick(View view) {
        addToCart();
    }

    private void addToFirebase(CartItem cartItem) {
        // Firebase reference to the user's cart
        DatabaseReference userCartReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("user_carts")
                .child(currentUser.getUid());

        // Create a unique key for the cart item in Firebase
        String cartItemKey = userCartReference.push().getKey();

        // Set the cart item in Firebase using the unique key
        userCartReference.child(cartItemKey).setValue(cartItem);
    }
}
