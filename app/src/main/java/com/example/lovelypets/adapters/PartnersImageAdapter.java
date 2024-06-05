package com.example.lovelypets.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovelypets.R;

/**
 * Adapter for displaying partner images in a RecyclerView.
 */
public class PartnersImageAdapter extends RecyclerView.Adapter<PartnersImageAdapter.ViewHolder> {
    private static final String TAG = "PartnersImageAdapter";
    private final int[] images;
    private final Context context;

    /**
     * Constructor for the PartnersImageAdapter.
     *
     * @param context The context from which the adapter is created.
     * @param images  The array of image resource IDs to display.
     */
    public PartnersImageAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
    }

    /**
     * Creates a new ViewHolder to hold the view for each partner image item.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the partner image item
        View view = LayoutInflater.from(context).inflate(R.layout.partners_image_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data to the ViewHolder for a specific position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the data item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the image resource for the ImageView
        holder.imageView.setImageResource(images[position]);
        Log.d(TAG, "Binding image at position: " + position);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return images.length;
    }

    /**
     * ViewHolder class for the partner image items.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The view of the partner image item.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize the ImageView
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
