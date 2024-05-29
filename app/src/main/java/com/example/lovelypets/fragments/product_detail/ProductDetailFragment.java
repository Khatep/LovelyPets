package com.example.lovelypets.fragments.product_detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovelypets.R;
import com.example.lovelypets.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends Fragment {
    private String iconName;
    private String name;
    private String description;
    private String categoryId;
    private Long price;
    private static final String ARG_ICON_NAME = "icon_name";
    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_PRICE = "price";
    private boolean isLiked = false;
    private String[] userId = {"default"};
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment ProductDetailFragment.
     */
    public static ProductDetailFragment newInstance(String iconName, String name, String description, String categoryId, Long price) {
        Log.d("Tag", "iconName :: " + iconName);
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ICON_NAME, iconName);
        args.putString(ARG_NAME, name);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_CATEGORY_ID, categoryId);
        args.putLong(ARG_PRICE, price);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String iconName = getArguments().getString(ARG_ICON_NAME);
            String name = getArguments().getString(ARG_NAME);
            String description = getArguments().getString(ARG_DESCRIPTION);
            String categoryId = getArguments().getString(ARG_CATEGORY_ID);
            Long price = getArguments().getLong(ARG_PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setUserId();
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ImageView favouriteIconView = view.findViewById(R.id.favourite_icon);
        ImageView productImageView = view.findViewById(R.id.product_imageview);
        TextView nameTextView = view.findViewById(R.id.product_name);
        TextView descriptionTextView = view.findViewById(R.id.product_description);
        TextView priceTextView = view.findViewById(R.id.product_price);
        Button addToCartButton = view.findViewById(R.id.add_to_basket);

        assert getArguments() != null;
        iconName = getArguments().getString(ARG_ICON_NAME);
        name = getArguments().getString(ARG_NAME);
        description = getArguments().getString(ARG_DESCRIPTION);
        categoryId = getArguments().getString(ARG_CATEGORY_ID);
        price = getArguments().getLong(ARG_PRICE);

        favouriteIconView.setImageResource(R.drawable.ic_baseline_favourite_default);
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        priceTextView.setText(String.valueOf(price) + " KZT");

        int imageId = this.getMinmapResIdByName(iconName);
        productImageView.setImageResource(imageId);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming userId is retrieved from a user session or similar method
                ///String userId ="default";  // Replace with actual method to get the user ID
                if(!userId[0].equals("default")) {
                    addProductToCart(userId[0]);
                }
                return;
            }
        });

        favouriteIconView.setOnClickListener(v -> {
            isLiked = !isLiked;
            if(isLiked) {
                favouriteIconView.setImageResource(R.drawable.ic_baseline_favourite_default);
            } else {
                favouriteIconView.setImageResource(R.drawable.ic_baseline_favourite_liked);
            }
        });

        return view;
    }

    public void addProductToCart(String userId) {
        Log.d("userId", userId);
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");
        Product product = new Product(iconName, name, description, categoryId, price);
        System.out.println(product);
        cartRef.push().setValue(product).addOnSuccessListener(unused -> {
            Log.d("add product to cart", "Successful");
            return;
        }).addOnFailureListener(e -> {
            Log.d("onFailure","Failure");
            e.printStackTrace();
        });
    }
    private void setUserId() {
        usersReference = firebaseDatabase.getReference().child("users");
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        assert userEmail != null;
        Log.d("User Email", userEmail);
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    assert snapshot != null;
                    System.out.println(snapshot);
                    if (snapshot.exists() && Objects.equals(snapshot.child("email").getValue(), userEmail)) {
                        Log.d("If state", "I am here3");
                        Log.d("Snapshot get key", snapshot.getKey());
                        userId[0] = snapshot.getKey();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "Connecting to userReference is fault");
            }
        });
        Log.d("After userReference listener", userId[0]);
        // Replace with actual logic to get the user ID
    }

    private int getMinmapResIdByName(String iconName) {
        String pkgName = requireContext().getPackageName();
        int resId = requireContext().getResources().getIdentifier(iconName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name : " + iconName + "==> Res Id = " + resId);
        return resId;
    }
}