package com.example.coffeeshop.splash.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.coffeeshop.R;
import com.example.coffeeshop.splash.adapetrss.CartAdapter;
import com.example.coffeeshop.splash.modal.CartItem;
import com.example.coffeeshop.splash.modal.CartManager;
import com.example.coffeeshop.splash.checkout.Checkout_Methods;

import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button buyNowButton;
    private CartAdapter cartAdapter;
    private TextView totalTextView;
    private TextView textView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyCartTextView; // Added TextView for empty cart message

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            updateTotalPrice();
            swipeRefreshLayout.setRefreshing(false);
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<CartItem> cartItems = CartManager.getInstance(requireContext()).getCartItems();
        cartAdapter = new CartAdapter(cartItems, requireContext());
        recyclerView.setAdapter(cartAdapter);

        cartAdapter.setUpItemTouchHelper(recyclerView);

        CartManager.getInstance(requireContext()).setCartChangeListener(() -> {
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
            updateCartViews(); // Added to update views based on cart size
        });

        textView = view.findViewById(R.id.textView);
        totalTextView = view.findViewById(R.id.total);
        emptyCartTextView = view.findViewById(R.id.emptyCartTextView); // Added TextView reference

        buyNowButton = view.findViewById(R.id.btn);
        buyNowButton.setOnClickListener(v -> {
//            CartManager.getInstance(requireContext()).clearCart();
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
            updateCartViews(); // Added to update views based on cart size

            // opening new activity on click
            Intent i = new Intent(getActivity(), Checkout_Methods.class);
            startActivity(i);


        });

        updateTotalPrice();
        updateCartViews(); // Added to update views based on cart size
    }

    private void updateTotalPrice() {
        if (isAdded()) {
            List<CartItem> cartItems = CartManager.getInstance(requireContext()).getCartItems();
            double totalPrice = 0;
            for (CartItem item : cartItems) {
                totalPrice += item.getTotalPrice();
            }
            totalTextView.setText(String.valueOf(totalPrice));
        }
    }

    private void updateCartViews() {
        if (isAdded() && isVisible()) {
            List<CartItem> cartItems = CartManager.getInstance(requireContext()).getCartItems();
            if (cartItems.isEmpty()) {
                buyNowButton.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                totalTextView.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                emptyCartTextView.setVisibility(View.VISIBLE); // Show empty cart message
            } else {
                buyNowButton.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                totalTextView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                emptyCartTextView.setVisibility(View.GONE); // Hide empty cart message
            }
        }
    }

}
