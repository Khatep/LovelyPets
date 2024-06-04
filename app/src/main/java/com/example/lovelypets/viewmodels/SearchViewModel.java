package com.example.lovelypets.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lovelypets.enums.ProductType;
import com.example.lovelypets.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchViewModel extends ViewModel {
    private final MutableLiveData<List<Product>> products = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Product>> filteredProducts = new MutableLiveData<>(new ArrayList<>());
    private boolean isCheckBoxFoodChecked = false;
    private boolean isCheckBoxCagesFeedersBowlsChecked = false;
    private boolean isCheckBoxFillersChecked = false;
    private boolean isCheckBoxToysChecked = false;
    private boolean isCheckBoxEquipmentChecked = false;
    private boolean isCheckBoxMedicinesChecked = false;
    private boolean isCheckBoxPetsChecked = false;
    private boolean isCheckBoxLeashChecked = false;

    public SearchViewModel() {
        loadProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public LiveData<List<Product>> getFilteredProducts() {
        return filteredProducts;
    }

    public void loadProducts() {
        FirebaseDatabase.getInstance().getReference().child("products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Product> productList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Product product;
                            product = new Product(
                                    Objects.requireNonNull(snapshot.child("iconName").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("name").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("description").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("categoryId").getValue()).toString(),
                                    Long.parseLong((String.valueOf(snapshot.child("price").getValue()))),
                                    ProductType.valueOf(Objects.requireNonNull(snapshot.child("productType").getValue()).toString())
                            );
                            if (product != null) {
                                productList.add(product);
                            }
                        }
                        products.setValue(productList);
                        filteredProducts.setValue(productList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }

    public void filterProducts(String query) {
        List<Product> productList = products.getValue();
        if (productList == null)
            return;

        List<Product> resultList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                resultList.add(product);
            }
        }

        filteredProducts.setValue(resultList);
    }

    public void applyFilters(List<ProductType> selectedTypes) {
        List<Product> productList = products.getValue();
        if (productList == null) return;

        List<Product> resultList = new ArrayList<>();
        for (Product product : productList) {
            for (ProductType type : selectedTypes) {
                if (type == ProductType.ALL || product.getProductType() == type) {
                    resultList.add(product);
                    break;
                }
            }
        }

        filteredProducts.setValue(resultList);
    }

    public boolean isCheckBoxFoodChecked() {
        return isCheckBoxFoodChecked;
    }

    public boolean isCheckBoxCagesFeedersBowlsChecked() {
        return isCheckBoxCagesFeedersBowlsChecked;
    }

    public boolean isCheckBoxFillersChecked() {
        return isCheckBoxFillersChecked;
    }

    public boolean isCheckBoxToysChecked() {
        return isCheckBoxToysChecked;
    }

    public boolean isCheckBoxEquipmentChecked() {
        return isCheckBoxEquipmentChecked;
    }

    public boolean isCheckBoxMedicinesChecked() {
        return isCheckBoxMedicinesChecked;
    }

    public boolean isCheckBoxPetsChecked() {
        return isCheckBoxPetsChecked;
    }

    public boolean isCheckBoxLeashChecked() {
        return isCheckBoxLeashChecked;
    }

    public void setFilterSelections(boolean isCheckBoxFoodChecked,
                                    boolean isCheckBoxCagesFeedersBowlsChecked,
                                    boolean isCheckBoxFillersChecked,
                                    boolean isCheckBoxToysChecked,
                                    boolean isCheckBoxEquipmentChecked,
                                    boolean isCheckBoxMedicinesChecked,
                                    boolean isCheckBoxPetsChecked,
                                    boolean isCheckBoxLeashChecked) {
        this.isCheckBoxFoodChecked = isCheckBoxFoodChecked;
        this.isCheckBoxCagesFeedersBowlsChecked = isCheckBoxCagesFeedersBowlsChecked;
        this.isCheckBoxFillersChecked = isCheckBoxFillersChecked;
        this.isCheckBoxToysChecked = isCheckBoxToysChecked;
        this.isCheckBoxEquipmentChecked = isCheckBoxEquipmentChecked;
        this.isCheckBoxMedicinesChecked = isCheckBoxMedicinesChecked;
        this.isCheckBoxPetsChecked = isCheckBoxPetsChecked;
        this.isCheckBoxLeashChecked = isCheckBoxLeashChecked;
    }
}
