package com.yqritc.recyclerviewflexibledivider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cundong.recyclerview.R;

/**
 *
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;

    private int h_spacing;

    private int v_spacing;

    private boolean includeEdge;

    private boolean hasHeader;

    Drawable mDivider;

    public GridSpacingItemDecoration(Context mContext, int spanCount, int h_spacing, int v_spacing, boolean includeEdge, boolean hasHeader) {
        this.spanCount = spanCount;
        this.h_spacing = h_spacing;
        this.v_spacing = v_spacing;
        this.includeEdge = includeEdge;
        this.hasHeader = hasHeader;

        mDivider = mContext.getResources().getDrawable(R.drawable.list_divider);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

//        final int left = parent.getPaddingLeft();
//        final int right = parent.getWidth() - parent.getPaddingRight();
//        final int childCount = parent.getChildCount();
//
//        System.out.println("childCount: " + childCount);
//
//        System.out.println("left: " + left + " right: " + right);

        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }

        int itemCount = adapter.getItemCount();
        int lastDividerOffset = getLastDividerOffset(parent);
        int validChildCount = parent.getChildCount();
        int lastChildPosition = -1;

        System.out.println("itemCount: " + itemCount + "  lastDividerOffset: " + lastDividerOffset + "  validChildCount: " + validChildCount);

//        for (int i = 0; i < validChildCount; i++) {
//            View child = parent.getChildAt(i);
//            int childPosition = parent.getChildAdapterPosition(child);
//
//            if (childPosition < lastChildPosition) {
//                // Avoid remaining divider when animation starts
//                continue;
//            }
//            lastChildPosition = childPosition;
//
//            if (!mShowLastDivider && childPosition >= itemCount - lastDividerOffset) {
//                // Don't draw divider for last line if mShowLastDivider = false
//                continue;
//            }
//
//            if (wasDividerAlreadyDrawn(childPosition, parent)) {
//                // No need to draw divider again as it was drawn already by previous column
//                continue;
//            }
//
//            int groupIndex = getGroupIndex(childPosition, parent);
//            if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)) {
//                continue;
//            }

//        Rect bounds = getDividerBound(groupIndex, parent, child);


    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (hasHeader) {

            if (position == 0) {
                outRect.set(0, 0, 0, 0);
                return;
            } else {
                position = position - 1;
            }
        }

        int column = position % spanCount;

        if (includeEdge) {
            outRect.left = h_spacing - column * h_spacing / spanCount;
            outRect.right = (column + 1) * h_spacing / spanCount;

            if (position < spanCount) {
                outRect.top = v_spacing;
            }
            outRect.bottom = v_spacing;
        } else {
            outRect.left = column * h_spacing / spanCount;
            outRect.right = h_spacing - (column + 1) * h_spacing / spanCount;
            if (position >= spanCount) {
                outRect.top = v_spacing;
            }
        }
    }


    private int getLastDividerOffset(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            int itemCount = parent.getAdapter().getItemCount();
            for (int i = itemCount - 1; i >= 0; i--) {
                if (spanSizeLookup.getSpanIndex(i, spanCount) == 0) {
                    return itemCount - i;
                }
            }
        }
        return 1;
    }
}