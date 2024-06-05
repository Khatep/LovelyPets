package com.example.lovelypets.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.eventlisteners.OnOrderClickListener;
import com.example.lovelypets.models.Order;

import java.util.List;

/**
 * ViewHolder class for displaying order items in a RecyclerView.
 */
public class OrderViewHolder extends RecyclerView.ViewHolder {
    public ImageView[] imageViews;
    public TextView statusTextView;
    public TextView orderNumberTextView;
    public LinearLayout orderDetailLayout;
    public CardView orderCardView;

    /**
     * Constructor for initializing the ViewHolder with the order view and context.
     *
     * @param context The context from which the ViewHolder is created.
     * @param orders The list of orders to be displayed.
     * @param orderView The view of the order item in the RecyclerView.
     * @param onOrderClickListener The listener to handle order click events.
     */
    public OrderViewHolder(Context context, List<Order> orders, @NonNull View orderView, OnOrderClickListener onOrderClickListener) {
        super(orderView);

        // Initialize UI components by finding them from the orderView.
        statusTextView = orderView.findViewById(R.id.order_status);
        orderNumberTextView = orderView.findViewById(R.id.order_number);
        orderDetailLayout = orderView.findViewById(R.id.order_detail_layout);
        orderCardView = orderView.findViewById(R.id.order_item_for_order_history_fragment);

        // Initialize the array of image views.
        imageViews = new ImageView[] {
                orderView.findViewById(R.id.product_image_1),
                orderView.findViewById(R.id.product_image_2),
                orderView.findViewById(R.id.product_image_3),
                orderView.findViewById(R.id.product_image_4),
                orderView.findViewById(R.id.product_image_5)
        };

        // Set a click listener on the orderCardView to handle order item clicks.
        orderCardView.setOnClickListener(v -> onOrderClickListener.onOrderClicked(orders.get(getAdapterPosition())));
    }
}
