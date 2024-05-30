package com.example.lovelypets.fragments.cart;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.ProductAdapterForCartFragment;
import com.example.lovelypets.event_listeners.OnDeleteIconClickListener;
import com.example.lovelypets.event_listeners.OnProductClickListener;
import com.example.lovelypets.fragments.product_detail.ProductDetailFragment;
import com.example.lovelypets.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment implements OnProductClickListener, OnDeleteIconClickListener {
    private RecyclerView cartRecycleView;
    private TextView totalPriceTextView;
    private List<Product> cartProductList;
    private DatabaseReference cartRef;
    private Button purchaseButton;
    private double totalPrice;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference;
    private String[] userId = {"default"};

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        usersReference = firebaseDatabase.getReference().child("users");
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartRecycleView = view.findViewById(R.id.cart_list_view);
        totalPriceTextView = view.findViewById(R.id.total_price_text);
        purchaseButton = view.findViewById(R.id.purchase_button);
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists() && Objects.equals(snapshot.child("email").getValue(), userEmail)) {
                        userId[0] = snapshot.getKey();
                        assert userId[0] != null;
                        Log.d("User ID", userId[0]);
                        cartProductList = getCartProductList();
                        setCartRecycleView();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        purchaseButton.setOnClickListener(v -> {
            showCustomDialog();
        });

        return view;
    }

    public void setCartRecycleView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);  // 2 columns
        cartRecycleView.setLayoutManager(gridLayoutManager);
        cartRecycleView.setAdapter(new ProductAdapterForCartFragment(getContext(), cartProductList, this, this));
    }

    public List<Product> getCartProductList() {
        List<Product> productList = new ArrayList<>();
        cartRef = FirebaseDatabase.getInstance().getReference("users").child(userId[0]).child("cart");
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartProductList.clear();
                totalPrice = 0.0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product;
                    product = new Product(
                            Objects.requireNonNull(snapshot.child("iconName").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("name").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("description").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("categoryId").getValue()).toString(),
                            Long.parseLong((String.valueOf(snapshot.child("price").getValue()))));

                    productList.add(product);
                    totalPrice += product.getPrice();
                }
                totalPriceTextView.setText("Total: " + (int) totalPrice + " KZT");

                if (cartRecycleView != null && cartRecycleView.getAdapter() != null) {
                    cartRecycleView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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

    @Override
    public void onDeleteIconClicked(Product product) {
        cartRef = FirebaseDatabase.getInstance().getReference("users").child(userId[0]).child("cart");
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String cartProductName = String.valueOf(childSnapshot.child("name").getValue());

                    System.out.println(cartProductName);
                    System.out.println(product.getName());

                    if (cartProductName.equals(product.getName())) {
                        // Product found! Delete it.
                        childSnapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            // Handle completion as before (success/failure)
                        });
                        break; // Exit loop after finding the product
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors in reading data
                Log.w("CartActivity", "Error reading cart data", error.toException());
            }
        });
    }

    public void showCustomDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.activity_payment_dialog);

      /*  TextView titleTextView = dialog.findViewById(R.id.dialogTitle);
        TextView messageTextView = dialog.findViewById(R.id.dialogMessage);
        titleTextView.setText("Anime Girl");
        messageTextView.setText("Do you like anime?");*/

        Button btn = dialog.findViewById(R.id.pay_button);
        btn.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }
}
