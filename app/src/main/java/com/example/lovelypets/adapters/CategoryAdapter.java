package com.example.lovelypets.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.models.Category;
import com.example.lovelypets.viewholders.CategoryViewHolder;

import java.util.List;

/**
 * Adapter for displaying categories in a RecyclerView.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private final List<Category> categories;
    private final OnItemClickListener listener;

    /**
     * Interface for handling category item clicks.
     */
    public interface OnItemClickListener {
        /**
         * Called when a category item is clicked.
         *
         * @param category The clicked category.
         */
        void onItemClick(Category category);
    }

    /**
     * Constructor for the CategoryAdapter.
     *
     * @param categories The list of categories to display.
     * @param listener   The listener for handling item clicks.
     */
    public CategoryAdapter(List<Category> categories, OnItemClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    /**
     * Creates a new ViewHolder to hold the view for each category item.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new CategoryViewHolder.
     */
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    /**
     * Binds the data to the ViewHolder for a specific position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the data item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.imageView.setImageResource(category.getIconId());
        holder.bind(category, listener);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }
}
