package com.example.lovelypets.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;
import com.example.lovelypets.holders.ProductViewHolder;
import com.example.lovelypets.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    Context context;
    List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(context, products, LayoutInflater.from(context).inflate(R.layout.product_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.nameView.setText(products.get(position).getName());

        String populationText = context.getString(R.string.population_label, products.get(position).getPrice());
        holder.populationView.setText(populationText);

        int imageId = this.getMinmapResIdByName(products.get(position).getIconName());
        holder.imageView.setImageResource(imageId);
    }

    private int getMinmapResIdByName(String iconName) {
        String pkgName = context.getPackageName();

        int resId = context.getResources().getIdentifier(iconName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name : " + iconName + "==> Res Id = " + resId);
        return resId;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
