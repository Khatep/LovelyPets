package com.example.lovelypets.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.eventlisteners.OnOrderClickListener;
import com.example.lovelypets.models.Order;
import com.example.lovelypets.viewholders.OrderViewHolder;

import java.util.List;

/**
 * Adapter for displaying orders in a RecyclerView.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    private static final String TAG = "OrderAdapter";
    private final Context context;
    private final List<Order> orders;
    private final OnOrderClickListener onOrderClickListener;

    /**
     * Constructor for the OrderAdapter.
     *
     * @param context              The context from which the adapter is created.
     * @param orders               The list of orders to display.
     * @param onOrderClickListener The listener for handling order item clicks.
     */
    public OrderAdapter(Context context, List<Order> orders, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.orders = orders;
        this.onOrderClickListener = onOrderClickListener;
    }

    /**
     * Creates a new ViewHolder to hold the view for each order item.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new OrderViewHolder.
     */
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(context, orders,
                LayoutInflater.from(context).inflate(R.layout.order_history_item,
                        parent, false), onOrderClickListener);
    }

    /**
     * Binds the data to the ViewHolder for a specific position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the data item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Set the order status text
        holder.statusTextView.setText(order.getOrderStatus().toString());

        // Set the order number text
        String orderNumberText = context.getString(R.string.order_number_label, order.getOrderNumber());
        holder.orderNumberTextView.setText(orderNumberText);

        // Set the click listener for the order card view
        holder.orderCardView.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                Order clickedOrder = orders.get(position);
                onOrderClickListener.onOrderClicked(clickedOrder);
                v.setBackgroundColor(Color.GRAY);
                new Handler().postDelayed(() -> v.setBackgroundColor(Color.TRANSPARENT), 100);
            }
        });

        // Set the images for the order's products
        int productCount = Math.min(order.getProducts().size(), 5);
        for (int i = 0; i < productCount; i++) {
            int imageId = getMinmapResIdByName(order.getProducts().get(i).getIconName());
            holder.imageViews[i].setImageResource(imageId);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return orders.size();
    }

    /**
     * Gets the resource ID for a drawable in the mipmap directory by its name.
     *
     * @param iconName The name of the drawable.
     * @return The resource ID of the drawable.
     */
    private int getMinmapResIdByName(String iconName) {
        String pkgName = context.getPackageName();
        int resId = context.getResources().getIdentifier(iconName, "mipmap", pkgName);
        Log.i(TAG, "Res Name : " + iconName + "==> Res Id = " + resId);
        return resId;
    }
}
