package com.example.lovelypets.fragments.home;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A custom {@link RecyclerView.ItemDecoration} that adds spacing between items in a RecyclerView.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "SpacesItemDecoration";

    private final int space;

    /**
     * Creates a new {@link SpacesItemDecoration}.
     *
     * @param space The amount of space to add around each item, in pixels.
     */
    public SpacesItemDecoration(int space) {
        this.space = space;
        Log.d(TAG, "SpacesItemDecoration initialized with space: " + space);
    }

    /**
     * Retrieve any offsets for the given item. Each field of {@code outRect} specifies the number of pixels
     * that the item view should be inset by, similar to padding or margin. The default implementation sets the
     * bounds to 0 and returns.
     *
     * @param outRect Rect to receive the output.
     * @param view    The child view to decorate
     * @param parent  RecyclerView this ItemDecoration is decorating
     * @param state   The current state of RecyclerView
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        // Set left, right, and bottom spacing for each item
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
            Log.d(TAG, "Adding top space for the first item");
        } else {
            outRect.top = 0;
        }

        Log.d(TAG, "Offsets set for item at position " + parent.getChildLayoutPosition(view) + ": " + outRect.toString());
    }
}
