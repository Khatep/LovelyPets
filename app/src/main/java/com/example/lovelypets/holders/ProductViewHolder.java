package com.example.lovelypets.holders;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lovelypets.R;
import com.example.lovelypets.models.Product;

import java.util.List;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    Context context;
    List<Product> products;
    public ImageView imageView;
    public TextView nameView;
    public TextView populationView;
    public ProductViewHolder(Context context, List<Product> products, @NonNull View productView) {
        super(productView);
        this.context = context;
        this.products = products;
        imageView = productView.findViewById(R.id.imageview);
        nameView = productView.findViewById(R.id.name);
        populationView = productView.findViewById(R.id.population);

        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Product clickedProduct = products.get(position);
                String countryName = clickedProduct.getName();

                productView.setBackgroundColor(Color.GRAY);
                new Handler().postDelayed(() -> {
                    productView.setBackgroundColor(Color.TRANSPARENT);
                }, 100);
                Toast.makeText(context, countryName, Toast.LENGTH_SHORT).show();
            }
        });
    }
}