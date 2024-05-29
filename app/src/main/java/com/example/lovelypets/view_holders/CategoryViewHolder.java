package com.example.lovelypets.view_holders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.CategoryAdapter;
import com.example.lovelypets.models.Category;
import com.google.android.material.imageview.ShapeableImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public ShapeableImageView imageView;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.listImage);
    }

    public void bind(final Category category, final CategoryAdapter.OnItemClickListener listener) {
        itemView.setOnClickListener(v -> listener.onItemClick(category));
    }
}