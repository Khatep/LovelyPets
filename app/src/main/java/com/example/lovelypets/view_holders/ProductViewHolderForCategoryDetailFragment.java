package com.example.lovelypets.view_holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.models.Product;

import java.util.List;

public class ProductViewHolderForCategoryDetailFragment extends RecyclerView.ViewHolder {
    Context context;
    List<Product> products;
    public ImageView imageView;
    public ImageView favouriteIconView;
    public TextView nameView;
    public TextView priceView;
    public CardView productCardViewForCategoryDetailFragment;
    public ProductViewHolderForCategoryDetailFragment(Context context, List<Product> products, @NonNull View productView) {
        super(productView);
        this.context = context;
        this.products = products;

        favouriteIconView = productView.findViewById(R.id.favourite);
        imageView = productView.findViewById(R.id.imageview);
        nameView = productView.findViewById(R.id.name);
        priceView = productView.findViewById(R.id.price);
        productCardViewForCategoryDetailFragment = productView.findViewById(R.id.product_item_for_category_detail_fragment);
    }
}