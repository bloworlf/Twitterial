package com.codepath.apps.restclienttemplate.others;

/*

Decorator which adds spacing around the tiles in a Grid layout RecyclerView. Apply to a RecyclerView with:
    SpacesItemDecoration decoration = new SpacesItemDecoration(16);
    mRecyclerView.addItemDecoration(decoration);

Feel free to add any value you wish for SpacesItemDecoration. That value determines the amount of spacing.
Source: http://blog.grafixartist.com/pinterest-masonry-layout-staggered-grid/
*/

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;
    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = mSpace;
    }
}
