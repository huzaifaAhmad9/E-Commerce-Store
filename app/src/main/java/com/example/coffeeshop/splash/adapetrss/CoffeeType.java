package com.example.coffeeshop.splash.adapetrss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshop.R;

import java.util.List;

public class CoffeeType extends RecyclerView.Adapter<CoffeeType.ViewHolder> {
    private List<String> buttonTitles;
    private OnItemClickListener listener;
    private int selectedPosition = -1;

    public CoffeeType(List<String> buttonTitles, OnItemClickListener listener) {
        this.buttonTitles = buttonTitles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffee_types, null ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = buttonTitles.get(position);
        holder.button.setText(title);
        holder.bind(title, listener);

        // Highlight the selected button
        holder.button.setBackgroundColor(position == selectedPosition
                ? holder.itemView.getContext().getResources().getColor(R.color.main)
                : holder.itemView.getContext().getResources().getColor(android.R.color.transparent));
    }

    @Override
    public int getItemCount() {
        return buttonTitles.size();
    }



    public interface OnItemClickListener {
        void onItemClick(String coffeeType);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
        }

        public void bind(final String coffeeType, final OnItemClickListener listener) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedPosition); // Unhighlight previously selected button
                    selectedPosition = getAdapterPosition();
                    notifyItemChanged(selectedPosition); // Highlight the newly selected button
                    listener.onItemClick(coffeeType);
                }
            });
        }
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }
}
