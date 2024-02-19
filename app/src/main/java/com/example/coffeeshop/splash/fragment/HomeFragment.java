package com.example.coffeeshop.splash.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.coffeeshop.R;
import com.example.coffeeshop.splash.adapetrss.CoffeeType;
import com.example.coffeeshop.splash.coffeeTypeFragments.Americano_Fragment;
import com.example.coffeeshop.splash.coffeeTypeFragments.Black_Tea_Fragment;
import com.example.coffeeshop.splash.coffeeTypeFragments.Cappuccino_Fragment;
import com.example.coffeeshop.splash.coffeeTypeFragments.Frappuccino_Fragment;
import com.example.coffeeshop.splash.coffeeTypeFragments.Latte_Fragment;
import com.example.coffeeshop.splash.coffeeTypeFragments.Mocha_Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements CoffeeType.OnItemClickListener {
    private ImageSlider imageSlider;
    private RecyclerView recyclerView;
    private CoffeeType adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider = view.findViewById(R.id.imgslider);
        recyclerView = view.findViewById(R.id.recView);

        List<String> buttonTitles = Arrays.asList("Cappuccino", "Mocha", "Latte", "Americano", "Frappuccino", "Tea");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CoffeeType(buttonTitles, this);
        recyclerView.setAdapter(adapter);

        // adding images to show
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.img1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img5, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img6, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img7, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        // Manually select Cappuccino initially
        onItemClick("Cappuccino");

        return view;
    }

    @Override
    public void onItemClick(String coffeeType) {
        // Handle item click, load the corresponding fragment
        switch (coffeeType) {
            case "Cappuccino":
                loadFragment(new Cappuccino_Fragment(), 0);
                break;
            case "Mocha":
                loadFragment(new Mocha_Fragment(), 1);
                break;
            case "Latte":
                loadFragment(new Latte_Fragment(), 2);
                break;
            case "Americano":
                loadFragment(new Americano_Fragment(), 3);
                break;
            case "Frappuccino":
                loadFragment(new Frappuccino_Fragment(), 4);
                break;
            case "Tea":
//                Toast.makeText(getContext(), "Coming Soon!!!!", Toast.LENGTH_SHORT).show();
                loadFragment(new Black_Tea_Fragment(), 5);
                break;
            // Add cases for other coffee types
            default:
                Toast.makeText(getActivity(), "Fragment not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragment(Fragment fragment, int position) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container1, fragment); // Ensure R.id.container1 is the correct ID
        transaction.commit();
        adapter.setSelectedPosition(position); // Highlight the selected button
    }
}
