package com.example.lovelypets.viewholders;

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

/**
 * ViewHolder class for displaying product items in the category detail fragment's RecyclerView.
 */
public class ProductViewHolderForCategoryDetailFragment extends RecyclerView.ViewHolder {
    Context context;
    List<Product> products;
    public ImageView imageView;
    public ImageView favouriteIconView;
    public TextView nameView;
    public TextView priceView;
    public CardView productCardViewForCategoryDetailFragment;

    /**
     * Constructor for initializing the ViewHolder with the product view and context.
     *
     * @param context The context from which the ViewHolder is created.
     * @param products The list of products to be displayed in the category detail fragment.
     * @param productView The view of the product item in the RecyclerView.
     */
    public ProductViewHolderForCategoryDetailFragment(Context context, List<Product> products, @NonNull View productView) {
        super(productView);
        this.context = context;
        this.products = products;

        // Initialize UI components by finding them from the productView.
        favouriteIconView = productView.findViewById(R.id.favourite);
        imageView = productView.findViewById(R.id.imageview);
        nameView = productView.findViewById(R.id.name);
        priceView = productView.findViewById(R.id.price);
        productCardViewForCategoryDetailFragment = productView.findViewById(R.id.product_item_for_category_detail_fragment);
    }
}