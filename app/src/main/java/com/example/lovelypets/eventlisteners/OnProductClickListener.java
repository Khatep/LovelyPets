package com.example.lovelypets.eventlisteners;

import com.example.lovelypets.models.Product;

/**
 * Listener interface to handle product click events.
 */
public interface OnProductClickListener {

    /**
     * Method to be called when a product is clicked.
     *
     * @param product The product that was clicked.
     */
    void onProductClicked(Product product);
}
