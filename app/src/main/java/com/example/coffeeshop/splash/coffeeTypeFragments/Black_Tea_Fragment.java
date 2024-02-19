package com.example.coffeeshop.splash.coffeeTypeFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coffeeshop.R;

public class Black_Tea_Fragment extends Fragment {
    LottieAnimationView animation;



    public Black_Tea_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_black__tea_, container, false);
        animation = view.findViewById(R.id.animation);
        animation.setAnimation(R.raw.comming_soon);
        animation.playAnimation();
        return view;
    }
}