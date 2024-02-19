package com.example.coffeeshop.splash.coffeeTypeFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.coffeeshop.R;


public class Frappuccino_Fragment extends Fragment {
    ImageButton card1,card2,card3,card4;


    public Frappuccino_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_frappuccino_, container, false);
        card1 = view.findViewById(R.id.card1);
        card2 = view.findViewById(R.id.card2);
        card3 = view.findViewById(R.id.card3);
        card4 = view.findViewById(R.id.card4);

        // Set a click listener for the card
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click
                navigateToAnotherFragment("Frap_Pumpkin");
            }
        });

        // Set a click listener for the card
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click
                navigateToAnotherFragment("Frap_Caramel");
            }
        });

        // Set a click listener for the card
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click
                Toast.makeText(getContext(), "Coming Soon!!!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set a click listener for the card
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click
                navigateToAnotherFragment("Frap_ButterBear");
            }
        });

        return view;

    }



    private void navigateToAnotherFragment(String activityName) {
        // Dynamically create an Intent based on the activityName
        try {
            Class<?> activityClass = Class.forName("com.example.coffeeshop.splash.coffeeDetailsActivity." + activityName);
            Intent i = new Intent(getActivity(), activityClass);
            startActivity(i);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}