package com.example.lovelypets.fragments.productdetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovelypets.R;
import com.example.lovelypets.enums.ProductType;
import com.example.lovelypets.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * {@link Fragment} subclass that displays detailed information about a specific product.
 */
public class ProductDetailFragment extends Fragment {
    private static final String TAG = "ProductDetailFragment";
    private static final String ARG_ICON_NAME = "icon_name";
    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_PRICE = "price";
    private static final String ARG_PRODUCT_TYPE = "product_type";

    private String iconName;
    private String name;
    private String description;
    private String categoryId;
    private Long price;
    private ProductType productType;
    private boolean isLiked = false;
    private final String[] userId = {"default"};
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of ProductDetailFragment.
     *
     * @param iconName     The name of the product icon.
     * @param name         The name of the product.
     * @param description  The description of the product.
     * @param categoryId   The ID of the product's category.
     * @param price        The price of the product.
     * @param productType  The type of the product.
     * @return A new instance of fragment ProductDetailFragment.
     */
    public static ProductDetailFragment newInstance(String iconName, String name, String description, String categoryId, Long price, ProductType productType) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ICON_NAME, iconName);
        args.putString(ARG_NAME, name);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_CATEGORY_ID, categoryId);
        args.putLong(ARG_PRICE, price);
        args.putString(ARG_PRODUCT_TYPE, productType.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            iconName = getArguments().getString(ARG_ICON_NAME);
            name = getArguments().getString(ARG_NAME);
            description = getArguments().getString(ARG_DESCRIPTION);
            categoryId = getArguments().getString(ARG_CATEGORY_ID);
            price = getArguments().getLong(ARG_PRICE);
            productType = ProductType.valueOf(getArguments().getString(ARG_PRODUCT_TYPE));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setUserId();
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ImageView favouriteIconView = view.findViewById(R.id.favourite_icon);
        ImageView productImageView = view.findViewById(R.id.product_imageview);
        TextView nameTextView = view.findViewById(R.id.product_name);
        TextView descriptionTextView = view.findViewById(R.id.product_description);
        TextView priceTextView = view.findViewById(R.id.product_price);
        Button addToCartButton = view.findViewById(R.id.add_to_basket);

        // Setting up the product details in the UI
        favouriteIconView.setImageResource(R.drawable.ic_baseline_favourite_default);
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        priceTextView.setText(String.format("%d KZT", price));

        int imageId = getMinmapResIdByName(iconName);
        productImageView.setImageResource(imageId);

        // Add to cart button click listener
        addToCartButton.setOnClickListener(v -> {
            if (!userId[0].equals("default")) {
                addProductToCart(userId[0]);
            }
        });

        // Favourite icon click listener to toggle like status
        favouriteIconView.setOnClickListener(v -> {
            isLiked = !isLiked;
            if (isLiked) {
                favouriteIconView.setImageResource(R.drawable.ic_baseline_favourite_liked);
            } else {
                favouriteIconView.setImageResource(R.drawable.ic_baseline_favourite_default);
            }
        });

        return view;
    }

    /**
     * Adds the current product to the user's cart in the Firebase database.
     *
     * @param userId The ID of the user.
     */
    private void addProductToCart(String userId) {
        Log.d(TAG, "addProductToCart: userId=" + userId);
        DatabaseReference cartRef = firebaseDatabase.getReference("users").child(userId).child("cart");
        Product product = new Product(iconName, name, description, categoryId, price, productType);
        cartRef.push().setValue(product).addOnSuccessListener(unused -> {
            Log.d(TAG, "addProductToCart: Product added to cart successfully.");
        }).addOnFailureListener(e -> {
            Log.e(TAG, "addProductToCart: Failed to add product to cart.", e);
        });
    }

    /**
     * Sets the user ID by retrieving it from the Firebase Authentication and Database.
     */
    private void setUserId() {
        usersReference = firebaseDatabase.getReference().child("users");
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        if (userEmail != null) {
            Log.d(TAG, "setUserId: userEmail=" + userEmail);
            usersReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists() && Objects.equals(snapshot.child("email").getValue(), userEmail)) {
                            userId[0] = snapshot.getKey();
                            Log.d(TAG, "setUserId: userId=" + userId[0]);
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "setUserId: Failed to connect to usersReference.", error.toException());
                }
            });
        } else {
            Log.e(TAG, "setUserId: userEmail is null.");
        }
    }

    /**
     * Retrieves the resource ID for the given icon name.
     *
     * @param iconName The name of the icon.
     * @return The resource ID of the icon.
     */
    private int getMinmapResIdByName(String iconName) {
        String pkgName = requireContext().getPackageName();
        int resId = requireContext().getResources().getIdentifier(iconName, "mipmap", pkgName);
        Log.i(TAG, "getMinmapResIdByName: Res Name : " + iconName + " ==> Res Id = " + resId);
        return resId;
    }
}
