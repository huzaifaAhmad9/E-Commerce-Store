package com.example.coffeeshop.splash.modal;

import java.util.Objects;

public class CartItem {
    private String name;
    private String type;
    private double price;
    private int quantity;
    private int imageResource;  // Assuming you have an image resource ID
    private double totalPrice;  // New field to store total price

    public CartItem(String name, String type, double price, int quantity, int imageResource) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.imageResource = imageResource;
        this.totalPrice = price * quantity;  // Calculate total price initially
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = price * quantity;  // Update total price when quantity changes
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CartItem cartItem = (CartItem) obj;

        return Double.compare(cartItem.price, price) == 0 &&
                quantity == cartItem.quantity &&
                imageResource == cartItem.imageResource &&
                Objects.equals(name, cartItem.name) &&
                Objects.equals(type, cartItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, price, quantity, imageResource);
    }
}
