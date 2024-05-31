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
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.viewholders.ProductViewHolderForCategoryDetailFragment;
import com.example.lovelypets.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapterForCategoryDetailFragment extends RecyclerView.Adapter<ProductViewHolderForCategoryDetailFragment> {
    Context context;
    List<Product> products;
    private boolean isLiked = false;
    private final OnProductClickListener onProductClickListener;

    public ProductAdapterForCategoryDetailFragment(Context context, List<Product> products, OnProductClickListener onProductClickListener ) {
        this.context = context;
        this.products = products;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolderForCategoryDetailFragment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolderForCategoryDetailFragment(context, products, LayoutInflater.from(context).inflate(R.layout.product_item_for_category_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolderForCategoryDetailFragment holder, int position) {
        holder.nameView.setText(products.get(position).getName());

        String priceText = context.getString(R.string.price_label, products.get(position).getPrice());
        holder.priceView.setText(priceText);

        int imageId = this.getMinmapResIdByName(products.get(position).getIconName());
        holder.imageView.setImageResource(imageId);

        holder.favouriteIconView.setOnClickListener(v -> {
            isLiked = !isLiked;
            if(isLiked) {
                holder.favouriteIconView.setImageResource(R.drawable.ic_baseline_favourite_default);
            } else {
                holder.favouriteIconView.setImageResource(R.drawable.ic_baseline_favourite_liked);
            }
        });

        holder.productCardViewForCategoryDetailFragment.setOnClickListener(v -> {
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


    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }
    public void setFilteredList(List<Product> filteredList) {
        this.products = filteredList;
        notifyDataSetChanged();
    }

    public void clearList() {
        this.products = new ArrayList<>();
        notifyDataSetChanged();
    }
}
