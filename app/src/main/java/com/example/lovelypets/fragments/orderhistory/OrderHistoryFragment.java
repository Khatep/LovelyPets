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
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFragment extends Fragment implements OnOrderClickListener {
    private RecyclerView orderRecycleView;
    private List<Order> orders = new ArrayList<>();
    private DatabaseReference ordersRef, usersReference;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String[] userId = {"default"};
    public OrderHistoryFragment() {
        // Required empty public constructor
    }

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
        usersReference = firebaseDatabase.getReference().child("users");
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        orderRecycleView = view.findViewById(R.id.order_list_view);
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
                //TODO
            }
        });
        return view;
    }

    private void loadOrderList() {
        ordersRef = firebaseDatabase.getReference("users").child(userId[0]).child("orders");
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
                                        Long.parseLong(Objects.requireNonNull(productSnapshot.child("price").getValue()).toString())
                                        //ProductType.valueOf(Objects.requireNonNull(productSnapshot.child("productType").getValue()).toString())
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
                            //TODO
                        }
                    });
                }
                setOrderRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void setOrderRecycleView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        orderRecycleView.setLayoutManager(gridLayoutManager);
        orderRecycleView.setAdapter(new OrderAdapter(requireContext(), orders, this));
    }

    @Override
    public void onOrderClicked(Order order) {
        OrderHistoryDetailFragment orderHistoryDetailFragment = OrderHistoryDetailFragment.newInstance(order);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, orderHistoryDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}