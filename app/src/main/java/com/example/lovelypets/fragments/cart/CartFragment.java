package com.example.lovelypets.fragments.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.ProductAdapterForCartFragment;
import com.example.lovelypets.eventlisteners.OnBackPressedListener;
import com.example.lovelypets.eventlisteners.OnDeleteIconClickListener;
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.exit_alert_dialog.ExitDialogActivity;
import com.example.lovelypets.fragments.productdetail.ProductDetailFragment;
import com.example.lovelypets.models.Product;
import com.example.lovelypets.payment.PaymentDialogActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment implements OnProductClickListener, OnDeleteIconClickListener, CartProductListProvider, OnBackPressedListener {
    private RecyclerView cartRecycleView;
    private TextView totalPriceTextView;
    private List<Product> cartProductList;
    private DatabaseReference cartRef, usersReference;
    private Button purchaseButton;
    private double totalPrice;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String[] userId = {"default"};
    private List<Product> productList = new ArrayList<>();
    private TextView cartIsEmptyTextView;

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
        cartIsEmptyTextView = view.findViewById(R.id.cart_is_empty_text);
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
                        cartProductList = loadCartProductList();
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

    public List<Product> loadCartProductList() {
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
    public List<Product> getProductList() {
        return productList;
    }

    @Override
    public double getTotalPrice() {
        return totalPrice;
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
        if (cartProductList.isEmpty()) {
            Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println(cartProductList.size());
            PaymentDialogActivity dialog = new PaymentDialogActivity(requireContext(), this);
            dialog.show();
        }
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
