package com.example.lovelypets.fragments.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.eventlisteners.OnBackPressedListener;
import com.example.lovelypets.exit_alert_dialog.ExitDialogActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CategoryFragment extends Fragment implements OnBackPressedListener {
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference categoriesReference;
    private  RecyclerView recyclerView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        categoriesReference = firebaseDatabase.getReference().child("categories");
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = requireView().findViewById(R.id.recyclerview);

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
        final String[] categoryId = new String[1];
        categoriesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists() && Objects.equals(snapshot.child("name").getValue(), name)) {
                        categoryId[0] = snapshot.getKey();
                    }
                }

                // Notify the adapter that data has changed
                if (recyclerView != null && recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cardView.setOnClickListener(v -> {
            CategoryDetailFragment detailFragment = CategoryDetailFragment.newInstance(categoryId[0], iconId, name, description);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    public void showExitDialog() {
        ExitDialogActivity dialog = new ExitDialogActivity(requireContext());
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }
}