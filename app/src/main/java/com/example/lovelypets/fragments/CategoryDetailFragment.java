package com.example.lovelypets.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.ProductAdapter;
import com.example.lovelypets.holders.ProductViewHolder;
import com.example.lovelypets.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryDetailFragment extends Fragment {
    private static final String ARG_ICON_ID = "icon_id";
    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private ImageView backImageView;

    public static CategoryDetailFragment newInstance(int iconId, String name, String description) {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ICON_ID, iconId);
        args.putString(ARG_NAME, name);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int iconId = getArguments().getInt(ARG_ICON_ID);
            String name = getArguments().getString(ARG_NAME);
            String description = getArguments().getString(ARG_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_detail, container, false);

        ImageView imageView = view.findViewById(R.id.category_icon);
        TextView nameView = view.findViewById(R.id.category_name);
        //TextView descriptionView = view.findViewById(R.id.category_description);

        backImageView = view.findViewById(R.id.back_arrow);

        backImageView.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        if (getArguments() != null) {
            int iconId = getArguments().getInt(ARG_ICON_ID);
            String name = getArguments().getString(ARG_NAME);
            String description = getArguments().getString(ARG_DESCRIPTION);

            imageView.setImageResource(iconId);
            nameView.setText(name);
            //descriptionView.setText(description);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        List<Product> products = new ArrayList<>();

        if (Objects.equals(getArguments().getString(ARG_NAME), "Cat")) {
            products.add(new Product("wiskas", getString(R.string.whiskas_85_1), "SS", "N1", 150L));
            products.add(new Product("kaz", "Kaz", "SS", "N1", 150L));
            products.add(new Product("rus", "Ru", "SS", "N1", 150L));
            products.add(new Product("arg", "Arg", "SS", "N1", 150L));
            products.add(new Product("bra", "Bra", "SS", "N1", 150L));
            products.add(new Product("kaz", "Kaz", "SS", "N1", 150L));
            products.add(new Product("rus", "Ru", "SS", "N1", 150L));
            products.add(new Product("arg", "Arg", "SS", "N1", 150L));
            products.add(new Product("bra", "Bra", "SS", "N1", 150L));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ProductAdapter(getContext(), products));
        return view;
    }
}
