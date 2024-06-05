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

/**
 * Adapter for displaying products in a cart fragment or order detail fragment.
 */
public class ProductAdapterForCartFragment extends RecyclerView.Adapter<ProductViewHolderForCartFragment> {

    private final Context context;
    private final List<Product> products;
    private final OnProductClickListener onProductClickListener;
    private final OnDeleteIconClickListener onDeleteIconClickListener;
    private final String whichFragment;

    /**
     * Constructor for the ProductAdapterForCartFragment.
     *
     * @param context                 The context from which the adapter is created.
     * @param products                The list of products to display.
     * @param onProductClickListener  The listener for product click events.
     * @param onDeleteIconClickListener The listener for delete icon click events.
     */
    public ProductAdapterForCartFragment(Context context, List<Product> products, OnProductClickListener onProductClickListener, OnDeleteIconClickListener onDeleteIconClickListener) {
        this.whichFragment = "CART_FRAGMENT";
        this.context = context;
        this.products = products;
        this.onProductClickListener = onProductClickListener;
        this.onDeleteIconClickListener = onDeleteIconClickListener;
    }

    /**
     * Constructor for the ProductAdapterForCartFragment without delete icon listener.
     *
     * @param context                The context from which the adapter is created.
     * @param products               The list of products to display.
     * @param onProductClickListener The listener for product click events.
     */
    public ProductAdapterForCartFragment(Context context, List<Product> products, OnProductClickListener onProductClickListener) {
        this.whichFragment = "ORDER_DETAIL_FRAGMENT";
        this.context = context;
        this.products = products;
        this.onProductClickListener = onProductClickListener;
        this.onDeleteIconClickListener = null;
    }

    /**
     * Creates a new ViewHolder to hold the view for each product item.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new ProductViewHolderForCartFragment.
     */
    @NonNull
    @Override
    public ProductViewHolderForCartFragment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_for_cart, parent, false);
        return new ProductViewHolderForCartFragment(context, products, view, onProductClickListener, onDeleteIconClickListener);
    }

    /**
     * Binds the data to the ViewHolder for a specific position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the data item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolderForCartFragment holder, int position) {
        Product product = products.get(position);
        holder.nameView.setText(product.getName());

        String priceText = context.getString(R.string.price_label, product.getPrice());
        holder.priceView.setText(priceText);

        int imageId = getMipmapResIdByName(product.getIconName());
        holder.imageView.setImageResource(imageId);

        holder.productCardViewForCartFragment.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                Product clickedProduct = products.get(position);
                Log.d("image view clicked", clickedProduct.toString());
                onProductClickListener.onProductClicked(clickedProduct);
                v.setBackgroundColor(Color.GRAY);
                new Handler().postDelayed(() -> v.setBackgroundColor(Color.TRANSPARENT), 100);
            }
        });

        if ("ORDER_DETAIL_FRAGMENT".equals(whichFragment)) {
            holder.deleteIconView.setVisibility(View.GONE);
        } else {
            holder.deleteIconView.setOnClickListener(v -> {
                Product clickedProduct = products.get(holder.getAdapterPosition());
                Log.d("delete icon clicked", clickedProduct.toString());
                if (onDeleteIconClickListener != null) {
                    onDeleteIconClickListener.onDeleteIconClicked(clickedProduct);
                }
            });
        }
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
        Log.i("CustomListView", "Res Name : " + iconName + "==> Res Id = " + resId);
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
}
