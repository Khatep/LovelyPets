package com.example.lovelypets.fragments.category;

import android.os.Bundle;
import android.util.Log;
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
import com.example.lovelypets.enums.ProductType;
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.fragments.productdetail.ProductDetailFragment;
import com.example.lovelypets.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@link Fragment} subclass representing the details of a specific category and the products under it.
 * Implements the {@link OnProductClickListener} to handle product clicked.
 */
public class CategoryDetailFragment extends Fragment implements OnProductClickListener {
    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_ICON_ID = "icon_id";
    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";

    private int iconId;
    private String name;
    private String description;
    private ImageView backImageView;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference productsReference;

    /**
     * Creates a new instance of CategoryDetailFragment.
     *
     * @param categoryId   The ID of the category.
     * @param iconId       The icon resource ID for the category.
     * @param name         The name of the category.
     * @param description  The description of the category.
     * @return A new instance of fragment CategoryDetailFragment.
     */
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
            iconId = getArguments().getInt(ARG_ICON_ID);
            name = getArguments().getString(ARG_NAME);
            description = getArguments().getString(ARG_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_detail, container, false);

        ImageView imageView = view.findViewById(R.id.category_icon);
        TextView nameView = view.findViewById(R.id.category_name);
        //TextView descriptionView = view.findViewById(R.id.category_description);

        backImageView = view.findViewById(R.id.back_arrow);
        backImageView.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        if (getArguments() != null) {
            iconId = getArguments().getInt(ARG_ICON_ID);
            name = getArguments().getString(ARG_NAME);
            description = getArguments().getString(ARG_DESCRIPTION);

            imageView.setImageResource(iconId);
            nameView.setText(name);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        List<Product> products = loadListOfProducts();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);  // 2 columns
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new ProductAdapterForCategoryDetailFragment(getContext(), products, this));

        return view;
    }

    /**
     * Loads the list of products under the specified category from the Firebase database.
     *
     * @return A list of products under the category.
     */
    private List<Product> loadListOfProducts() {
        List<Product> productList = new ArrayList<>();
        productsReference = firebaseDatabase.getReference().child("products");
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        if (snapshot.exists() && Objects.equals(snapshot.child("categoryId").getValue().toString(), requireArguments().getString(ARG_CATEGORY_ID))) {
                            Product product = new Product(
                                    Objects.requireNonNull(snapshot.child("iconName").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("name").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("description").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("categoryId").getValue()).toString(),
                                    Long.parseLong(Objects.requireNonNull(snapshot.child("price").getValue()).toString()),
                                    ProductType.valueOf(Objects.requireNonNull(snapshot.child("productType").getValue()).toString())
                            );
                            productList.add(product);
                        }
                    } catch (NullPointerException e) {
                        Log.e("CategoryDetailFragment", "Error parsing product data", e);
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
                // Log database error
                Log.e("CategoryDetailFragment", "Database error: " + databaseError.getMessage());
            }
        });

        return productList;
    }

    @Override
    public void onProductClicked(Product product) {
        ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(
                product.getIconName(), product.getName(), product.getDescription(),
                product.getCategoryId(), product.getPrice(), product.getProductType());

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, productDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
