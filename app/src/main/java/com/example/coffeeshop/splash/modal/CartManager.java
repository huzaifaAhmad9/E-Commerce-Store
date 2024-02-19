// CartManager.java
package com.example.coffeeshop.splash.modal;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String PREF_NAME = "CartPreferences";
    private static final String KEY_CART_ITEMS = "cartItems";

    private static CartManager instance;
    private List<CartItem> cartItemList;
    private CartChangeListener cartChangeListener;
    private SharedPreferences preferences;

    private CartManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        cartItemList = loadCartItems();
    }

    public static synchronized CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context);
        }
        return instance;
    }

    public List<CartItem> getCartItems() {
        return cartItemList;
    }

    public void addItemToCart(CartItem cartItem) {
        // Check if the item is already in the cart
        boolean isItemInCart = false;
        for (CartItem existingItem : cartItemList) {
            if (existingItem.getType().equals(cartItem.getType()) &&
                    existingItem.getName().equals(cartItem.getName())) {
                // Item is already in the cart, update quantity
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                isItemInCart = true;
                break;
            }
        }
        // If not in the cart, add a new instance of the item
        if (!isItemInCart) {
            CartItem newItem = new CartItem(cartItem.getName(), cartItem.getType(), cartItem.getPrice(), 1, cartItem.getImageResource());
            cartItemList.add(newItem);
        }
        notifyCartChanged(); // Notify cart change
        saveCartItems(); // Save cart items to SharedPreferences
    }

    public void removeItemFromCart(int position) {
        if (position >= 0 && position < cartItemList.size()) {
            cartItemList.remove(position);
            notifyCartChanged(); // Notify cart change
            saveCartItems(); // Save cart items to SharedPreferences
        }
    }

    public void restoreItemToCart(CartItem cartItem, int position) {
        if (position >= 0 && position <= cartItemList.size()) {
            cartItemList.add(position, cartItem);
            notifyCartChanged(); // Notify cart change
            saveCartItems(); // Save cart items to SharedPreferences
        }
    }

    public void clearCart() {
        cartItemList.clear();
        notifyCartChanged(); // Notify cart change
        saveCartItems(); // Save cart items to SharedPreferences
    }

    public interface CartChangeListener {
        void onCartChanged();
    }

    public void setCartChangeListener(CartChangeListener listener) {
        this.cartChangeListener = listener;
    }

    private void notifyCartChanged() {
        if (cartChangeListener != null) {
            cartChangeListener.onCartChanged();
        }
    }

    private void saveCartItems() {
        // Save the cart items to SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        JSONArray jsonArray = new JSONArray();
        for (CartItem item : cartItemList) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", item.getName());
                jsonObject.put("type", item.getType());
                jsonObject.put("price", item.getPrice());
                jsonObject.put("quantity", item.getQuantity());
                jsonObject.put("imageResource", item.getImageResource());
                jsonArray.put(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString(KEY_CART_ITEMS, jsonArray.toString());
        editor.apply();
    }

    private List<CartItem> loadCartItems() {
        // Load the cart items from SharedPreferences
        List<CartItem> loadedItems = new ArrayList<>();
        String cartItemsJson = preferences.getString(KEY_CART_ITEMS, "");
        if (!cartItemsJson.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(cartItemsJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                    String name = jsonObject.getString("name");
                    String type = jsonObject.getString("type");
                    double price = jsonObject.getDouble("price");
                    int quantity = jsonObject.getInt("quantity");
                    int imageResource = jsonObject.getInt("imageResource");
                    loadedItems.add(new CartItem(name, type, price, quantity, imageResource));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return loadedItems;
    }
}

