package com.example.lovelypets.fragments.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.PartnersImageAdapter;
import com.example.lovelypets.adapters.ProductAdapterForCategoryDetailFragment;
import com.example.lovelypets.enums.ProductType;
import com.example.lovelypets.eventlisteners.OnBackPressedListener;
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.exitalertdialog.ExitDialogActivity;
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

public class HomeFragment extends Fragment implements OnBackPressedListener, OnProductClickListener {
    private List<Product> popularProducts;
    private  RecyclerView popularProductsRecyclerView;
    private final String[] popularProductsIds = new String[] {
              "-NzUB8tbmT7LAihsXULJ", "-NzUB8tfCsVGS4-Kq9YQ", "-NzUB8tn_l4CxvJ1b34o",
            "-NzUB8uFj4DynjVX15Mo", "-NzUB8torE4MJPx8CC_0", "-NzUB8uDtaH7LBY4SRwh"
    };

    private DatabaseReference productsReference;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        popularProducts = loadPopularProductList();
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        int[] images = {
                R.drawable.life_planet,
                R.drawable.samsung,
                R.drawable.doctor_vet,
                R.drawable.kaspi,
                R.drawable.pet_care,
                R.drawable.dr_pets,
                R.drawable.iams,
                R.drawable.bonnyville,
                R.drawable.my_pets_kz,
                R.drawable.iitu,
                R.drawable.murkel
        };

        RecyclerView partnersRecyclerView = view.findViewById(R.id.partners_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        partnersRecyclerView.setLayoutManager(layoutManager);
        PartnersImageAdapter adapter = new PartnersImageAdapter(requireContext(), images);
        partnersRecyclerView.setAdapter(adapter);
        partnersRecyclerView.addItemDecoration(new SpacesItemDecoration(10));

        popularProductsRecyclerView = view.findViewById(R.id.products_list_view);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        popularProductsRecyclerView.setLayoutManager(layoutManager1);

        ProductAdapterForCategoryDetailFragment productAdapterForCategoryDetailFragment = new ProductAdapterForCategoryDetailFragment(requireContext(), popularProducts, this);
        popularProductsRecyclerView.setAdapter(productAdapterForCategoryDetailFragment);

        TextView highQualityQuestionTextView = view.findViewById(R.id.high_quality_products_question);
        TextView highQualityAnswerTextView = view.findViewById(R.id.high_quality_products_question_answer);
        TextView convenientServiceQuestionTextView = view.findViewById(R.id.convenient_service_question);
        TextView convenientServiceAnswerTextView = view.findViewById(R.id.convenient_service_answer);
        TextView discountsFromPartnersQuestionTextView = view.findViewById(R.id.discounts_from_partners_question);
        TextView discountsFromPartnersAnswerTextView = view.findViewById(R.id.discounts_from_partners_answer);
        TextView supportQuestionTextView = view.findViewById(R.id.support_24_7_question);
        TextView supportAnswerTextView = view.findViewById(R.id.support_24_7_answer);


        highQualityQuestionTextView.setOnClickListener(v -> {
            if (highQualityAnswerTextView.getVisibility() == View.GONE) {
                highQualityAnswerTextView.setVisibility(View.VISIBLE);
            } else {
                highQualityAnswerTextView.setVisibility(View.GONE);
            }
        });

        convenientServiceQuestionTextView.setOnClickListener(v -> {
            if (convenientServiceAnswerTextView.getVisibility() == View.GONE) {
                convenientServiceAnswerTextView.setVisibility(View.VISIBLE);
            } else {
                convenientServiceAnswerTextView.setVisibility(View.GONE);
            }
        });

        discountsFromPartnersQuestionTextView.setOnClickListener(v -> {
            if (discountsFromPartnersAnswerTextView.getVisibility() == View.GONE) {
                discountsFromPartnersAnswerTextView.setVisibility(View.VISIBLE);
            } else {
                discountsFromPartnersAnswerTextView.setVisibility(View.GONE);
            }
        });

        supportQuestionTextView.setOnClickListener(v -> {
            if (supportAnswerTextView.getVisibility() == View.GONE) {
                supportAnswerTextView.setVisibility(View.VISIBLE);
            } else {
                supportAnswerTextView.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private List<Product> loadPopularProductList() {
        List<Product> products = new ArrayList<>();
        DatabaseReference productsReference = firebaseDatabase.getReference().child("products");

        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String id : popularProductsIds) {
                    DataSnapshot snapshot = dataSnapshot.child(id);

                    if (!snapshot.exists()) {
                        Log.e("loadPopularProductList", "Product ID " + id + " does not exist in the database.");
                        continue;
                    }

                    String iconName = snapshot.child("iconName").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String categoryId = snapshot.child("categoryId").getValue(String.class);
                    Long price = snapshot.child("price").getValue(Long.class);
                    String productTypeString = snapshot.child("productType").getValue(String.class);

                    if (iconName == null || name == null || description == null || categoryId == null || price == null || productTypeString == null) {
                        Log.e("loadPopularProductList", "One of the fields for product ID " + id + " is null.");
                        continue;
                    }

                    ProductType productType;
                    try {
                        productType = ProductType.valueOf(productTypeString);
                    } catch (IllegalArgumentException e) {
                        Log.e("loadPopularProductList", "Invalid product type for product ID " + id);
                        continue;
                    }

                    Product product = new Product(iconName, name, description, categoryId, price, productType);
                    products.add(product);
                }

                if (popularProductsRecyclerView != null && popularProductsRecyclerView.getAdapter() != null) {
                    popularProductsRecyclerView.getAdapter().notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("loadPopularProductList", "Database error: " + error.getMessage());
            }
        });

        return products;
    }

    public void showExitDialog() {
        ExitDialogActivity dialog = new ExitDialogActivity(requireContext());
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
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