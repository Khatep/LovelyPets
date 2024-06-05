package com.example.lovelypets.fragments.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

/**
 * {@link Fragment} subclass representing the home screen of the app.
 * This fragment displays a list of popular products and partner logos.
 */
public class HomeFragment extends Fragment implements OnBackPressedListener, OnProductClickListener {

    private static final String TAG = "HomeFragment";

    private List<Product> popularProducts;
    private RecyclerView popularProductsRecyclerView;

    private static final String[] POPULAR_PRODUCT_IDS = {
            "-NzUB8tbmT7LAihsXULJ", "-NzUB8tfCsVGS4-Kq9YQ", "-NzUB8tn_l4CxvJ1b34o",
            "-NzUB8uFj4DynjVX15Mo", "-NzUB8torE4MJPx8CC_0", "-NzUB8uDtaH7LBY4SRwh"
    };

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        popularProducts = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Load and display partner logos
        setupPartnersRecyclerView(view);

        // Load and display popular products
        setupPopularProductsRecyclerView(view);

        // Setup question and answer toggle views
        setupQuestionAnswerToggles(view);

        // Load the popular product list from the database
        loadPopularProductList();

        return view;
    }

    /**
     * Sets up the RecyclerView for displaying partner logos.
     *
     * @param view The parent view containing the RecyclerView.
     */
    private void setupPartnersRecyclerView(View view) {
        int[] images = {
                R.drawable.life_planet, R.drawable.samsung, R.drawable.doctor_vet,
                R.drawable.kaspi, R.drawable.pet_care, R.drawable.dr_pets,
                R.drawable.iams, R.drawable.bonnyville, R.drawable.my_pets_kz,
                R.drawable.iitu, R.drawable.murkel
        };

        RecyclerView partnersRecyclerView = view.findViewById(R.id.partners_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        partnersRecyclerView.setLayoutManager(layoutManager);

        PartnersImageAdapter adapter = new PartnersImageAdapter(requireContext(), images);
        partnersRecyclerView.setAdapter(adapter);
        partnersRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
    }

    /**
     * Sets up the RecyclerView for displaying popular products.
     *
     * @param view The parent view containing the RecyclerView.
     */
    private void setupPopularProductsRecyclerView(View view) {
        popularProductsRecyclerView = view.findViewById(R.id.products_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        popularProductsRecyclerView.setLayoutManager(layoutManager);

        ProductAdapterForCategoryDetailFragment productAdapter = new ProductAdapterForCategoryDetailFragment(requireContext(), popularProducts, this);
        popularProductsRecyclerView.setAdapter(productAdapter);
    }

    /**
     * Sets up the question and answer toggle views.
     *
     * @param view The parent view containing the question and answer views.
     */
    private void setupQuestionAnswerToggles(View view) {
        setupToggle(view, R.id.high_quality_products_question, R.id.high_quality_products_question_answer);
        setupToggle(view, R.id.convenient_service_question, R.id.convenient_service_answer);
        setupToggle(view, R.id.discounts_from_partners_question, R.id.discounts_from_partners_answer);
        setupToggle(view, R.id.support_24_7_question, R.id.support_24_7_answer);
    }

    /**
     * Sets up a toggle view for a question and answer pair.
     *
     * @param view The parent view containing the question and answer views.
     * @param questionId The resource ID of the question TextView.
     * @param answerId The resource ID of the answer TextView.
     */
    private void setupToggle(View view, int questionId, int answerId) {
        TextView questionTextView = view.findViewById(questionId);
        TextView answerTextView = view.findViewById(answerId);

        questionTextView.setOnClickListener(v -> {
            if (answerTextView.getVisibility() == View.GONE) {
                answerTextView.setVisibility(View.VISIBLE);
            } else {
                answerTextView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Loads the list of popular products from the Firebase database.
     */
    private void loadPopularProductList() {
        DatabaseReference productsReference = firebaseDatabase.getReference().child("products");

        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                popularProducts.clear();

                for (String id : POPULAR_PRODUCT_IDS) {
                    DataSnapshot snapshot = dataSnapshot.child(id);

                    if (!snapshot.exists()) {
                        Log.e(TAG, "Product ID " + id + " does not exist in the database.");
                        continue;
                    }

                    String iconName = snapshot.child("iconName").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String categoryId = snapshot.child("categoryId").getValue(String.class);
                    Long price = snapshot.child("price").getValue(Long.class);
                    String productTypeString = snapshot.child("productType").getValue(String.class);

                    if (iconName == null || name == null || description == null || categoryId == null || price == null || productTypeString == null) {
                        Log.e(TAG, "One of the fields for product ID " + id + " is null.");
                        continue;
                    }

                    ProductType productType;
                    try {
                        productType = ProductType.valueOf(productTypeString);
                    } catch (IllegalArgumentException e) {
                        Log.e(TAG, "Invalid product type for product ID " + id);
                        continue;
                    }

                    Product product = new Product(iconName, name, description, categoryId, price, productType);
                    popularProducts.add(product);
                }

                if (popularProductsRecyclerView.getAdapter() != null) {
                    popularProductsRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }

    /**
     * Shows the exit dialog when the back button is pressed.
     */
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