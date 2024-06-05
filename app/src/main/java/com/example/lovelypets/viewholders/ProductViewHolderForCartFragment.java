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

/**
 * ViewHolder class for displaying product items in the cart fragment's RecyclerView.
 */
public class ProductViewHolderForCartFragment extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public ImageView deleteIconView;
    public TextView nameView;
    public TextView priceView;
    public CardView productCardViewForCartFragment;

    /**
     * Constructor for initializing the ViewHolder with the product view and context.
     *
     * @param context The context from which the ViewHolder is created.
     * @param products The list of products to be displayed in the cart.
     * @param productView The view of the product item in the RecyclerView.
     * @param onProductClickListener The listener to handle product click events.
     * @param onDeleteIconClickListener The listener to handle delete icon click events.
     */
    public ProductViewHolderForCartFragment(Context context, List<Product> products, @NonNull View productView, OnProductClickListener onProductClickListener, OnDeleteIconClickListener onDeleteIconClickListener) {
        super(productView);

        // Initialize UI components by finding them from the productView.
        deleteIconView = productView.findViewById(R.id.delete_icon);
        imageView = productView.findViewById(R.id.cart_item_image);
        nameView = productView.findViewById(R.id.cart_item_name);
        priceView = productView.findViewById(R.id.cart_item_price);
        productCardViewForCartFragment = productView.findViewById(R.id.product_cardView_for_cart_fragment);

        // Set a click listener on the productCardViewForCartFragment to handle product item clicks.
        productCardViewForCartFragment.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onProductClickListener.onProductClicked(products.get(position));
            }
        });

        // Set a click listener on the deleteIconView to handle delete icon clicks.
        deleteIconView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onDeleteIconClickListener.onDeleteIconClicked(products.get(position));
            }
        });
    }
}