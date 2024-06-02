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
import com.example.lovelypets.eventlisteners.OnDeleteIconClickListener;
import com.example.lovelypets.eventlisteners.OnOrderClickListener;
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.models.Order;
import com.example.lovelypets.models.Product;
import com.example.lovelypets.viewholders.OrderViewHolder;
import com.example.lovelypets.viewholders.ProductViewHolderForCartFragment;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    Context context;
    List<Order> orders;
    private final OnOrderClickListener onOrderClickListener;

    public OrderAdapter(Context context, List<Order> orders, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.orders = orders;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          return new OrderViewHolder(context, orders,
                  LayoutInflater.from(context).inflate(R.layout.order_history_item,
                  parent, false), onOrderClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.statusTextView.setText(orders.get(position).getOrderStatus().toString());

        String orderNumberText = context.getString(R.string.order_number_label, orders.get(position).getOrderNumber());
        holder.orderNumberTextView.setText(orderNumberText);

        holder.orderCardView.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                Order clickedOrder = orders.get(position);
                onOrderClickListener.onOrderClicked(clickedOrder);
                v.setBackgroundColor(Color.GRAY);
                new Handler().postDelayed(() -> {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }, 100);
            }
        });

        for (int i = 0; i < orders.get(position).getProducts().size() && i < 5; i++) {
            int imageId = this.getMinmapResIdByName(orders.get(position).getProducts().get(i).getIconName()); //
            holder.imageViews[i].setImageResource(imageId);
        }
    }

    private int getMinmapResIdByName(String iconName) {
        String pkgName = context.getPackageName();
        int resId = context.getResources().getIdentifier(iconName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name : " + iconName + "==> Res Id = " + resId);
        return resId;
    }
    @Override
    public int getItemCount() {
        return orders.size();
    }
}
