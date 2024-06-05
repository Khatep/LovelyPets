package com.example.lovelypets.eventlisteners;

import com.example.lovelypets.models.Order;

/**
 * Listener interface to handle order click events.
 */
public interface OnOrderClickListener {

    /**
     * Method to be called when an order is clicked.
     *
     * @param order The order that was clicked.
     */
    void onOrderClicked(Order order);
}
