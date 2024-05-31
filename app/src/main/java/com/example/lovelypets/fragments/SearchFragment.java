package com.example.lovelypets.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.ProductAdapterForCategoryDetailFragment;
import com.example.lovelypets.enums.ProductType;
import com.example.lovelypets.eventlisteners.OnBackPressedListener;
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.exit_alert_dialog.ExitDialogActivity;
import com.example.lovelypets.fragments.productdetail.ProductDetailFragment;
import com.example.lovelypets.models.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SearchFragment extends Fragment implements OnProductClickListener, OnBackPressedListener {
    private SearchView searchView;
    private List<Product> searchProductList;
    private RecyclerView searchRecycleView;
    private DatabaseReference productsReference;
    private ProductAdapterForCategoryDetailFragment productAdapterForCategoryDetailFragment;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private TextView dataNotFoundTextView;
    private ImageButton filterImageButton;
    private BottomSheetDialog bottomSheetDialog;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();

        filterImageButton = view.findViewById(R.id.filter_image);
        dataNotFoundTextView = view.findViewById(R.id.data_not_found_text);

        searchRecycleView = view.findViewById(R.id.search_list_view);
        searchProductList = loadListOfProducts();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);  // 2 columns
        searchRecycleView.setLayoutManager(gridLayoutManager);

        productAdapterForCategoryDetailFragment= new ProductAdapterForCategoryDetailFragment(getContext(), searchProductList, this);
        searchRecycleView.setAdapter(productAdapterForCategoryDetailFragment);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText, ProductType.ALL);
                return false;
            }
        });

        filterImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterImageButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_red_color), PorterDuff.Mode.SRC_IN);
                final View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
                bottomSheetDialog = new BottomSheetDialog(requireContext());
                bottomSheetDialog.setContentView(dialogView);

                CheckBox checkBoxFood = dialogView.findViewById(R.id.checkbox_food);
                //TODO set filter
                bottomSheetDialog.show();
            }
        });



        return view;
    }

    private void filterList(String newText, ProductType... productType) {
        List<Product> filteredProductList = new ArrayList<>();

        try {
            for (Product p : searchProductList) {
                if (p.getName().toLowerCase().contains(newText.toLowerCase())) {
                    filteredProductList.add(p);
                }
            }

            if (filteredProductList.isEmpty()) { //|| !searchProductList.isEmpty()
                System.out.println(searchProductList.toString());
                productAdapterForCategoryDetailFragment.clearList();
                dataNotFoundTextView.setVisibility(View.VISIBLE);

                if (searchProductList.isEmpty()) {
                    searchProductList = loadListOfProducts();
                    productAdapterForCategoryDetailFragment.setProducts(searchProductList);
                    dataNotFoundTextView.setVisibility(View.GONE);
                }
            } else {
                productAdapterForCategoryDetailFragment.setProducts(filteredProductList);
                dataNotFoundTextView.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            return;
        }
    }

    private List<Product> loadListOfProducts() {
        List<Product> productList = new ArrayList<>();
        productsReference = firebaseDatabase.getReference().child("products");
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product;
                        product = new Product(
                                Objects.requireNonNull(snapshot.child("iconName").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("name").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("description").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("categoryId").getValue()).toString(),
                                Long.parseLong((String.valueOf(snapshot.child("price").getValue()))));

                        productList.add(product);
                    }

                if (searchRecycleView != null && searchRecycleView.getAdapter() != null) {
                    searchRecycleView.getAdapter().notifyDataSetChanged();
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

        if (searchView != null)
            searchView.setQuery("", false);

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