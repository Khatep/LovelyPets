package com.example.lovelypets.fragments.cart;

import com.example.lovelypets.models.Product;

import java.util.List;

public interface CartProductListProvider {
    List<Product> getProductList();
    double getTotalPrice();
}