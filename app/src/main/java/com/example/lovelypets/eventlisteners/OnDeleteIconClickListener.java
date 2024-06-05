package com.example.lovelypets.eventlisteners;

import com.example.lovelypets.models.Product;

/**
 * Listener interface to handle delete icon click events for products.
 */
public interface OnDeleteIconClickListener {

    /**
     * Method to be called when the delete icon for a product is clicked.
     *
     * @param product The product associated with the delete icon that was clicked.
     */
    void onDeleteIconClicked(Product product);
}
