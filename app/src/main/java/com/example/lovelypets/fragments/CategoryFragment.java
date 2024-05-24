package com.example.lovelypets.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.lovelypets.R;

public class CategoryFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView catCardView = view.findViewById(R.id.cat_cardView);
        CardView dogCardView = view.findViewById(R.id.dog_cardView);
        CardView fishCardView = view.findViewById(R.id.fish_cardView);
        CardView hamsterCardView = view.findViewById(R.id.hamster_cardView);

        setupCard(catCardView, R.drawable.ic_baseline_animals_category_cat, "Cat", "Cat Description");
        setupCard(dogCardView, R.drawable.ic_baseline_animals_category_dog, "Dog", "Dog Description");
        setupCard(fishCardView, R.drawable.ic_baseline_animals_category_fish, "Fish", "Fish Description");
        setupCard(hamsterCardView, R.drawable.ic_baseline_animals_category_hamster_, "Hamster", "Hamster Description");
    }

    private void setupCard(CardView cardView, int iconId, String name, String description) {
        cardView.setOnClickListener(v -> {
            CategoryDetailFragment detailFragment = CategoryDetailFragment.newInstance(iconId, name, description);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
}
