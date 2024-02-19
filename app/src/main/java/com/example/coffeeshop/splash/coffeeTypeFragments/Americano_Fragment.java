package com.example.coffeeshop.splash.coffeeTypeFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.coffeeshop.R;


public class Americano_Fragment extends Fragment {
    ImageButton card1,card2,card3;



    public Americano_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_americano_, container, false);
        card1 = view.findViewById(R.id.card1);
        card2 = view.findViewById(R.id.card2);
        card3 = view.findViewById(R.id.card3);
        // Set a click listener for the card
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click
                navigateToAnotherFragment("Am_Caramel");
            }
        });

        // Set a click listener for the card
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click
                navigateToAnotherFragment("Am_Cupcake");
            }
        });

        // Set a click listener for the card
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click
                navigateToAnotherFragment("Am_Almond");
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