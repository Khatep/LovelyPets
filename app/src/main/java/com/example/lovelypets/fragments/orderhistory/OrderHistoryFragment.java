package com.example.lovelypets.fragments.orderhistory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.OrderAdapter;
import com.example.lovelypets.enums.ProductType;
import com.example.lovelypets.eventlisteners.OnOrderClickListener;
import com.example.lovelypets.models.Order;
import com.example.lovelypets.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@link Fragment} subclass that to display the order history of a user.
 */
public class OrderHistoryFragment extends Fragment implements OnOrderClickListener {
    private RecyclerView orderRecycleView;
    private final List<Order> orders = new ArrayList<>();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final String[] userId = {"default"};

    /**
     * Required empty public constructor
     */
    public OrderHistoryFragment() {}

    /**
     * Factory method to create a new instance of this fragment.
     */
    public static OrderHistoryFragment newInstance() {
        return new OrderHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setTitle("History");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DatabaseReference usersReference = firebaseDatabase.getReference().child("users");
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        orderRecycleView = view.findViewById(R.id.order_list_view);

        // Retrieve userId based on the user's email
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists() && Objects.equals(snapshot.child("email").getValue(), userEmail)) {
                        userId[0] = snapshot.getKey();
                        assert userId[0] != null;
                        loadOrderList();
                        setOrderRecycleView();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log error
                Log.e("OrderHistoryFragment", "Error fetching user data: " + error.getMessage());
            }
        });
        return view;
    }

    /**
     * Loads the order list from the database for the current user.
     */
    private void loadOrderList() {
        DatabaseReference ordersRef = firebaseDatabase.getReference("users").child(userId[0]).child("orders");

        // Retrieve orders for the current user
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int orderNumber = Integer.parseInt(Objects.requireNonNull(snapshot.child("orderNumber").getValue()).toString());
                    LocalDate dateOfCreated = LocalDate.of(
                            Integer.parseInt(Objects.requireNonNull(snapshot.child("dateOfCreated").child("year").getValue()).toString()),
                            Integer.parseInt(Objects.requireNonNull(snapshot.child("dateOfCreated").child("monthValue").getValue()).toString()),
                            Integer.parseInt(Objects.requireNonNull(snapshot.child("dateOfCreated").child("dayOfMonth").getValue()).toString())
                    );
                    String addressOfDelivery = Objects.requireNonNull(snapshot.child("addressOfDelivery").getValue()).toString();
                    double totalPrice = Double.parseDouble(Objects.requireNonNull(snapshot.child("totalPrice").getValue()).toString());

                    Order order = new Order(orderNumber, dateOfCreated, addressOfDelivery, totalPrice);

                    // Fetch products for each order
                    DatabaseReference orderProductsRef = snapshot.child("products").getRef();
                    orderProductsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<Product> products = new ArrayList<>();
                            for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                                Product product = new Product(
                                        Objects.requireNonNull(productSnapshot.child("iconName").getValue()).toString(),
                                        Objects.requireNonNull(productSnapshot.child("name").getValue()).toString(),
                                        Objects.requireNonNull(productSnapshot.child("description").getValue()).toString(),
                                        Objects.requireNonNull(productSnapshot.child("categoryId").getValue()).toString(),
                                        Long.parseLong(Objects.requireNonNull(productSnapshot.child("price").getValue()).toString()),
                                        ProductType.valueOf(Objects.requireNonNull(productSnapshot.child("productType").getValue()).toString())
                                );
                                products.add(product);
                            }
                            order.getProducts().addAll(products);
                            if (!orders.contains(order)) {
                                orders.add(order);
                            }
                            orderRecycleView.getAdapter().notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Log error
                            Log.e("OrderHistoryFragment", "Error fetching products: " + error.getMessage());
                        }
                    });
                }
                setOrderRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log error
                Log.e("OrderHistoryFragment", "Error fetching orders: " + error.getMessage());
            }
        });
    }

    /**
     * Sets up the RecyclerView for displaying the order list.
     */
    private void setOrderRecycleView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        orderRecycleView.setLayoutManager(gridLayoutManager);
        orderRecycleView.setAdapter(new OrderAdapter(requireContext(), orders, this));
    }

    @Override
    public void onOrderClicked(Order order) {
        // Handle order click event and navigate to the order detail fragment
        OrderHistoryDetailFragment orderHistoryDetailFragment = OrderHistoryDetailFragment.newInstance(order);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, orderHistoryDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
