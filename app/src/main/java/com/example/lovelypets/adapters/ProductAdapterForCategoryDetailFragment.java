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

/**
 * Adapter class for displaying products in a category detail fragment.
 */
public class ProductAdapterForCategoryDetailFragment extends RecyclerView.Adapter<ProductViewHolderForCategoryDetailFragment> {

    private final Context context;
    private List<Product> products;
    private boolean isLiked = false;
    private final OnProductClickListener onProductClickListener;

    /**
     * Constructor for the ProductAdapterForCategoryDetailFragment.
     *
     * @param context                The context from which the adapter is created.
     * @param products               The list of products to display.
     * @param onProductClickListener The listener for product click events.
     */
    public ProductAdapterForCategoryDetailFragment(Context context, List<Product> products, OnProductClickListener onProductClickListener) {
        this.context = context;
        this.products = products;
        this.onProductClickListener = onProductClickListener;
    }

    /**
     * Creates a new ViewHolder to hold the view for each product item.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new ProductViewHolderForCategoryDetailFragment.
     */
    @NonNull
    @Override
    public ProductViewHolderForCategoryDetailFragment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolderForCategoryDetailFragment(context, products, LayoutInflater.from(context).inflate(R.layout.product_item_for_category_detail, parent, false));
    }

    /**
     * Binds the data to the ViewHolder for a specific position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the data item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolderForCategoryDetailFragment holder, int position) {
        Product product = products.get(position);
        holder.nameView.setText(product.getName());

        String priceText = context.getString(R.string.price_label, product.getPrice());
        holder.priceView.setText(priceText);

        int imageId = getMipmapResIdByName(product.getIconName());
        holder.imageView.setImageResource(imageId);

        // Handle favourite icon click event
        holder.favouriteIconView.setOnClickListener(v -> {
            isLiked = !isLiked;
            if (isLiked) {
                holder.favouriteIconView.setImageResource(R.drawable.ic_baseline_favourite_default);
            } else {
                holder.favouriteIconView.setImageResource(R.drawable.ic_baseline_favourite_liked);
            }
        });

        // Handle product card click event
        holder.productCardViewForCategoryDetailFragment.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                Product clickedProduct = products.get(position);
                Log.d("image view clicked", clickedProduct.toString());
                onProductClickListener.onProductClicked(clickedProduct);
                v.setBackgroundColor(Color.GRAY);
                new Handler().postDelayed(() -> v.setBackgroundColor(Color.TRANSPARENT), 100);
            }
        });
    }

    /**
     * Gets the resource ID for a mipmap resource by name.
     *
     * @param iconName The name of the mipmap resource.
     * @return The resource ID.
     */
    private int getMipmapResIdByName(String iconName) {
        String pkgName = context.getPackageName();
        int resId = context.getResources().getIdentifier(iconName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name : " + iconName + " ==> Res Id = " + resId);
        return resId;
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * Sets a new list of products and notifies the adapter to refresh the data.
     *
     * @param products The new list of products.
     */
    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    /**
     * Sets a filtered list of products and notifies the adapter to refresh the data.
     *
     * @param filteredList The filtered list of products.
     */
    public void setFilteredList(List<Product> filteredList) {
        this.products = filteredList;
        notifyDataSetChanged();
    }

    /**
     * Clears the list of products and notifies the adapter to refresh the data.
     */
    public void clearList() {
        this.products = new ArrayList<>();
        notifyDataSetChanged();
    }
}