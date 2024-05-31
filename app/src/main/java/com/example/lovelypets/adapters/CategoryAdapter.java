package com.example.lovelypets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.models.Category;
import com.example.lovelypets.viewholders.CategoryViewHolder;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private final List<Category> categories;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public CategoryAdapter(Context context, List<Category> categories,  OnItemClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.imageView.setImageResource(category.getIconId());
        holder.bind(category, listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
