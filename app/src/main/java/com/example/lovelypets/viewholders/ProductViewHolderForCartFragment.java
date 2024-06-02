package com.example.lovelypets.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.eventlisteners.OnDeleteIconClickListener;
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.models.Product;

import java.util.List;

public class ProductViewHolderForCartFragment extends RecyclerView.ViewHolder {
    private final OnProductClickListener onProductClickListener;
    private final OnDeleteIconClickListener onDeleteIconClickListener;
    Context context;
    List<Product> products;
    public ImageView imageView;
    public ImageView deleteIconView;
    public TextView nameView;
    public TextView priceView;
    public CardView productCardViewForCartFragment;

    public ProductViewHolderForCartFragment(Context context, List<Product> products, @NonNull View productView, OnProductClickListener onProductClickListener, OnDeleteIconClickListener onDeleteIconClickListener) {
        super(productView);
        this.context = context;
        this.products = products;
        this.onProductClickListener = onProductClickListener;
        this.onDeleteIconClickListener = onDeleteIconClickListener;

        deleteIconView = productView.findViewById(R.id.delete_icon);
        imageView = productView.findViewById(R.id.cart_item_image);
        nameView = productView.findViewById(R.id.cart_item_name);
        priceView = productView.findViewById(R.id.cart_item_price);
        productCardViewForCartFragment = productView.findViewById(R.id.product_cardView_for_cart_fragment);
    }
}