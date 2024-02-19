// CartAdapter.java
package com.example.coffeeshop.splash.adapetrss;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshop.R;
import com.example.coffeeshop.splash.modal.CartItem;
import com.example.coffeeshop.splash.modal.CartManager;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;

    public CartAdapter(List<CartItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_to_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem currentItem = cartItemList.get(position);

        holder.itemName.setText(currentItem.getName());
        holder.itemType.setText(currentItem.getType());
        holder.itemQuantity.setText(String.valueOf(currentItem.getQuantity()));

        // Calculate and set the total price
        double totalPrice = currentItem.getPrice() * currentItem.getQuantity();
        holder.itemPrice.setText(String.valueOf(totalPrice));

        // Load image using Picasso
        Picasso.get().load(currentItem.getImageResource()).into(holder.itemImage);

        // Plus button click listener
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = currentItem.getQuantity();
                quantity++;
                currentItem.setQuantity(quantity);

                // Calculate and set the new total price
                double totalPrice = currentItem.getPrice() * quantity;
                currentItem.setTotalPrice(totalPrice);

                notifyDataSetChanged(); // Update the UI
            }
        });

        // Minus button click listener
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = currentItem.getQuantity();
                if (quantity > 1) {
                    quantity--;
                    currentItem.setQuantity(quantity);

                    // Calculate and set the new total price
                    double totalPrice = currentItem.getPrice() * quantity;
                    currentItem.setTotalPrice(totalPrice);

                    notifyDataSetChanged(); // Update the UI
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemType, itemPrice, itemQuantity;
        ImageView itemImage, plus, minus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.name);
            itemType = itemView.findViewById(R.id.type);
            itemPrice = itemView.findViewById(R.id.price);
            itemQuantity = itemView.findViewById(R.id.quantity);
            itemImage = itemView.findViewById(R.id.image);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
        }
    }

    public void setUpItemTouchHelper(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private final ColorDrawable background = new ColorDrawable(Color.RED);

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Remove item from the cart using the CartManager
                CartItem removedItem = cartItemList.get(position);
                CartManager.getInstance(context).removeItemFromCart(position);

                // Show Snackbar with an undo option
                Snackbar snackbar = Snackbar.make(recyclerView, "Item deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Undo the delete action
                                CartManager.getInstance(context).restoreItemToCart(removedItem, position);
                            }
                        });

                // Set the undo action text color to brown
                snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.main));
                snackbar.show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX < 0) { // Swipe to the left (delete)
                        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.delColor))
                                .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                                .addSwipeLeftLabel("Delete")
                                .setSwipeLeftLabelColor(ContextCompat.getColor(context, R.color.white))
                                .create()
                                .decorate();
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
