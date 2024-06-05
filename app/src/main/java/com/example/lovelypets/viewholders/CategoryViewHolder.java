package com.example.lovelypets.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.CategoryAdapter;
import com.example.lovelypets.models.Category;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * ViewHolder class for displaying category items in a RecyclerView.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public ShapeableImageView imageView;

    /**
     * Constructor for initializing the ViewHolder with the item view.
     *
     * @param itemView The view of the item in the RecyclerView.
     */
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        // Initialize the imageView by finding it from the itemView.
        imageView = itemView.findViewById(R.id.listImage);
    }

    /**
     * Binds the category data to the view and sets up the click listener.
     *
     * @param category The category data to be bound to the view.
     * @param listener The listener to handle item click events.
     */
    public void bind(final Category category, final CategoryAdapter.OnItemClickListener listener) {
        // Set a click listener on the itemView to handle category item clicks.
        itemView.setOnClickListener(v -> listener.onItemClick(category));
    }
}
