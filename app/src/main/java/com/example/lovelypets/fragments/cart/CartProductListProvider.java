package com.example.lovelypets.fragments.cart;

import com.example.lovelypets.models.Product;

import java.util.List;

/**
 * Interface to provide the list of products in the cart and the total price.
 */
public interface CartProductListProvider {

    /**
     * Gets the list of products in the cart.
     *
     * @return A list of {@link Product} objects representing the products in the cart.
     */
    List<Product> getProductList();

    /**
     * Gets the total price of the products in the cart.
     *
     * @return The total price as a double.
     */
    double getTotalPrice();
}