package com.example.lovelypets.fragments.search;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.ProductAdapterForCategoryDetailFragment;
import com.example.lovelypets.enums.ProductType;
import com.example.lovelypets.eventlisteners.OnBackPressedListener;
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.exitalertdialog.ExitDialogActivity;
import com.example.lovelypets.fragments.productdetail.ProductDetailFragment;
import com.example.lovelypets.models.Product;
import com.example.lovelypets.viewmodels.SearchViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 *{@link Fragment} subclass that displays searching and filtering products.
 */
public class SearchFragment extends Fragment implements OnProductClickListener, OnBackPressedListener {
    private SearchView searchView;
    private List<Product> searchProductList = new ArrayList<>();
    private RecyclerView searchRecycleView;
    private ProductAdapterForCategoryDetailFragment productAdapterForCategoryDetailFragment;
    private TextView dataNotFoundTextView;
    private ImageButton filterImageButton;
    private BottomSheetDialog bottomSheetDialog;
    private SearchViewModel searchViewModel;

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();

        filterImageButton = view.findViewById(R.id.filter_image);
        dataNotFoundTextView = view.findViewById(R.id.data_not_found_text);
        searchRecycleView = view.findViewById(R.id.search_list_view);

        // Initialize ViewModel
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Set up RecyclerView with GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);  // 2 columns
        searchRecycleView.setLayoutManager(gridLayoutManager);

        productAdapterForCategoryDetailFragment = new ProductAdapterForCategoryDetailFragment(getContext(), searchProductList, this);
        searchRecycleView.setAdapter(productAdapterForCategoryDetailFragment);

        // Observe the product list from the ViewModel
        searchViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            searchProductList = products;
            productAdapterForCategoryDetailFragment.setProducts(products);
        });

        // Observe filtered products and handle visibility of the "data not found" message
        searchViewModel.getFilteredProducts().observe(getViewLifecycleOwner(), filteredProducts -> {
            if (filteredProducts.isEmpty()) {
                dataNotFoundTextView.setVisibility(View.VISIBLE);
            } else {
                dataNotFoundTextView.setVisibility(View.GONE);
            }
            productAdapterForCategoryDetailFragment.setProducts(filteredProducts);
        });

        // Set up SearchView query listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchViewModel.filterProducts(newText);
                return false;
            }
        });

        // Set up filter button click listener
        filterImageButton.setOnClickListener(v -> showFilterDialog());

        return view;
    }

    /**
     * Handles product click events and navigates to the product detail fragment.
     */
    @Override
    public void onProductClicked(Product product) {
        ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(
                product.getIconName(), product.getName(), product.getDescription(),
                product.getCategoryId(), product.getPrice(), product.getProductType());
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, productDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        if (searchView != null)
            searchView.setQuery("", false);
    }

    /**
     * Displays the filter dialog for product types.
     */
    private void showFilterDialog() {
        filterImageButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_red_color), PorterDuff.Mode.SRC_IN);
        final View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(dialogView);

        CheckBox checkBoxFood = dialogView.findViewById(R.id.checkbox_food);
        CheckBox checkBoxCagesFeedersBowls = dialogView.findViewById(R.id.checkbox_cages_feeders_bowls);
        CheckBox checkBoxFillers = dialogView.findViewById(R.id.checkbox_fillers);
        CheckBox checkBoxToys = dialogView.findViewById(R.id.checkbox_toys);
        CheckBox checkBoxEquipment = dialogView.findViewById(R.id.checkbox_equipment);
        CheckBox checkBoxMedicines = dialogView.findViewById(R.id.checkbox_medicines);
        CheckBox checkBoxPets = dialogView.findViewById(R.id.checkbox_pets);
        CheckBox checkBoxLeash = dialogView.findViewById(R.id.checkbox_leash);

        // Set checkboxes according to ViewModel state
        checkBoxFood.setChecked(searchViewModel.isCheckBoxFoodChecked());
        checkBoxCagesFeedersBowls.setChecked(searchViewModel.isCheckBoxCagesFeedersBowlsChecked());
        checkBoxFillers.setChecked(searchViewModel.isCheckBoxFillersChecked());
        checkBoxToys.setChecked(searchViewModel.isCheckBoxToysChecked());
        checkBoxEquipment.setChecked(searchViewModel.isCheckBoxEquipmentChecked());
        checkBoxMedicines.setChecked(searchViewModel.isCheckBoxMedicinesChecked());
        checkBoxPets.setChecked(searchViewModel.isCheckBoxPetsChecked());
        checkBoxLeash.setChecked(searchViewModel.isCheckBoxLeashChecked());

        bottomSheetDialog.show();

        dialogView.findViewById(R.id.button_apply_filter).setOnClickListener(v1 -> {
            List<ProductType> selectedTypes = new ArrayList<>();
            if (checkBoxFood.isChecked()) selectedTypes.add(ProductType.FOODS);
            if (checkBoxCagesFeedersBowls.isChecked()) selectedTypes.add(ProductType.CAGES_FEEDERS_BOWLS);
            if (checkBoxFillers.isChecked()) selectedTypes.add(ProductType.FILLERS);
            if (checkBoxToys.isChecked()) selectedTypes.add(ProductType.TOYS);
            if (checkBoxEquipment.isChecked()) selectedTypes.add(ProductType.EQUIPMENT);
            if (checkBoxMedicines.isChecked()) selectedTypes.add(ProductType.MEDICINES);
            if (checkBoxPets.isChecked()) selectedTypes.add(ProductType.PET);
            if (checkBoxLeash.isChecked()) selectedTypes.add(ProductType.LEASH);

            if (selectedTypes.isEmpty()) {
                selectedTypes.add(ProductType.ALL);
                filterImageButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.weak_black), PorterDuff.Mode.SRC_IN);
            }

            // Save filter selections in the ViewModel
            searchViewModel.setFilterSelections(
                    checkBoxFood.isChecked(),
                    checkBoxCagesFeedersBowls.isChecked(),
                    checkBoxFillers.isChecked(),
                    checkBoxToys.isChecked(),
                    checkBoxEquipment.isChecked(),
                    checkBoxMedicines.isChecked(),
                    checkBoxPets.isChecked(),
                    checkBoxLeash.isChecked()
            );

            // Apply filters
            searchViewModel.applyFilters(selectedTypes);

            bottomSheetDialog.dismiss();
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
}
