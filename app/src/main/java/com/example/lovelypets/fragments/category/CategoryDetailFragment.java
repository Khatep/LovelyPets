package com.example.lovelypets.fragments.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.ProductAdapterForCategoryDetailFragment;
import com.example.lovelypets.event_listeners.OnProductClickListener;
import com.example.lovelypets.fragments.product_detail.ProductDetailFragment;
import com.example.lovelypets.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryDetailFragment extends Fragment implements OnProductClickListener {
    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_ICON_ID = "icon_id";
    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private ImageView backImageView;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference productsReference;

    public static CategoryDetailFragment newInstance(String categoryId, int iconId, String name, String description) {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_ID, categoryId);
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
        List<Product> products = getListOfProducts();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);  // 2 columns
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new ProductAdapterForCategoryDetailFragment(getContext(), products, this));

        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private List<Product> getListOfProducts() {
        List<Product> productList = new ArrayList<>();
        productsReference = firebaseDatabase.getReference().child("products");
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    assert getArguments() != null;
                    if (snapshot.exists() && (Objects.requireNonNull(snapshot.child("categoryId").getValue()).toString()).equals( Objects.requireNonNull(requireArguments().getString(ARG_CATEGORY_ID)))) {
                        Product product;
                        product = new Product(
                                Objects.requireNonNull(snapshot.child("iconName").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("name").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("description").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("categoryId").getValue()).toString(),
                                Long.parseLong((String.valueOf(snapshot.child("price").getValue()))));

                        productList.add(product);
                    }
                }

                // Notify the adapter that data has changed
                RecyclerView recyclerView = getView().findViewById(R.id.recyclerview);
                if (recyclerView != null && recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        return productList;
    }

    @Override
    public void onProductClicked(Product product) {
        ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(
                product.getIconName(), product.getName(), product.getDescription(),
                product.getCategoryId(), product.getPrice());

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, productDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}