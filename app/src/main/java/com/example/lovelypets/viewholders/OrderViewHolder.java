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

public class OrderViewHolder extends RecyclerView.ViewHolder {
    private final OnOrderClickListener onOrderClickListener;
    Context context;
    List<Order> orders;
    public ImageView[] imageViews;
    public TextView statusTextView;
    public TextView orderNumberTextView;
    public LinearLayout orderDetailLayout;
    public CardView orderCardView;

    public OrderViewHolder(Context context, List<Order> orders, @NonNull View orderView, OnOrderClickListener onOrderClickListener) {
        super(orderView);
        this.context = context;
        this.orders = orders;
        this.onOrderClickListener = onOrderClickListener;

        statusTextView = orderView.findViewById(R.id.order_status);
        orderNumberTextView = orderView.findViewById(R.id.order_number);
        orderDetailLayout = orderView.findViewById(R.id.order_detail_layout);
        orderCardView = orderView.findViewById(R.id.order_item_for_order_history_fragment);

        imageViews = new ImageView[] {
                orderView.findViewById(R.id.product_image_1),
                orderView.findViewById(R.id.product_image_2),
                orderView.findViewById(R.id.product_image_3),
                orderView.findViewById(R.id.product_image_4),
                orderView.findViewById(R.id.product_image_5),
        };

    }
}
