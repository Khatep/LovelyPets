package com.example.lovelypets.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.eventlisteners.OnDeleteIconClickListener;
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.models.Product;
import com.example.lovelypets.viewholders.ProductViewHolderForCartFragment;

import java.util.List;

public class ProductAdapterForCartFragment extends RecyclerView.Adapter<ProductViewHolderForCartFragment>{
    Context context;
    List<Product> products;
    private final OnProductClickListener onProductClickListener;
    private final OnDeleteIconClickListener onDeleteIconClickListener;
    private String whichFragment;

    public ProductAdapterForCartFragment(Context context, List<Product> products, OnProductClickListener onProductClickListener, OnDeleteIconClickListener onDeleteIconClickListener) {
        this.whichFragment = "CART_FRAGMENT";
        this.context = context;
        this.products = products;
        this.onProductClickListener = onProductClickListener;
        this.onDeleteIconClickListener = onDeleteIconClickListener;
    }

    public ProductAdapterForCartFragment(Context context, List<Product> products, OnProductClickListener onProductClickListener) {
        this.whichFragment = "ORDER_DETAIL_FRAGMENT";
        this.context = context;
        this.products = products;
        this.onProductClickListener = onProductClickListener;
        this.onDeleteIconClickListener = null;
    }

    @NonNull
    @Override
    public ProductViewHolderForCartFragment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolderForCartFragment(context, products,
                LayoutInflater.from(context).inflate(R.layout.product_item_for_cart,
                parent, false), onProductClickListener, onDeleteIconClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolderForCartFragment holder, int position) {
        holder.nameView.setText(products.get(position).getName());

        String priceText = context.getString(R.string.price_label, products.get(position).getPrice());
        holder.priceView.setText(priceText);

        int imageId = this.getMinmapResIdByName(products.get(position).getIconName());
        holder.imageView.setImageResource(imageId);

        holder.productCardViewForCartFragment.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                Product clickedProduct = products.get(position);
                Log.d("image view clicked", clickedProduct.toString());

                //navigateToProductDetail(clickedProduct);
                onProductClickListener.onProductClicked(clickedProduct);
                v.setBackgroundColor(Color.GRAY);
                new Handler().postDelayed(() -> {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }, 100);
            }
        });

        if (whichFragment.equals("ORDER_DETAIL_FRAGMENT")) {
            holder.deleteIconView.setVisibility(View.GONE);
        }
        holder.deleteIconView.setOnClickListener(v -> {
                Product clickedProduct = products.get(holder.getAdapterPosition());
                Log.d("delete icon clicked", clickedProduct.toString());
                onDeleteIconClickListener.onDeleteIconClicked(clickedProduct);
            });
    }

    private int getMinmapResIdByName(String iconName) {
        String pkgName = context.getPackageName();
        int resId = context.getResources().getIdentifier(iconName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name : " + iconName + "==> Res Id = " + resId);
        return resId;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
