package com.lnyp.flexibledivider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lnyp.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.lnyp.recyclerview.R;

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

//    Drawable mDividerVertical;

    public GridSpacingItemDecoration(Context mContext, int spanCount, int h_spacing, int v_spacing, boolean includeEdge, boolean hasHeader) {
        this.spanCount = spanCount;
        this.h_spacing = h_spacing;
        this.v_spacing = v_spacing;
        this.includeEdge = includeEdge;
        this.hasHeader = hasHeader;

        mDivider = mContext.getResources().getDrawable(R.drawable.list_divider);

//        mDividerVertical = mContext.getResources().getDrawable(R.drawable.list_divider_vertical);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        HeaderAndFooterRecyclerViewAdapter mAdapter = (HeaderAndFooterRecyclerViewAdapter) parent.getAdapter();

        if (mAdapter == null) {
            return;
        }

        int itemCount = mAdapter.getItemCount();

        for (int i = 0; i < itemCount; i++) {

            View child = parent.getChildAt(i);

            if (child != null) {

                int childPosition = parent.getChildAdapterPosition(child);

                if (hasHeader) {
                    if (childPosition == 0) {
                        continue;
                    } else {
                        childPosition = childPosition - 1;
                    }

                    if (mAdapter.isFooter(childPosition + 1)) {
                        continue;
                    }

                } else {

                    if (mAdapter.isFooter(childPosition)) {
                        continue;
                    }
                }

                if (includeEdge) {


                } else {

                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                    final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
                    final int right = left + h_spacing;
                    final int top = child.getTop() + params.topMargin + Math.round(ViewCompat.getTranslationY(child));
                    final int bottom = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));

                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);

                    if (childPosition >= spanCount) {

                        final int left1 = child.getLeft() + params.leftMargin + Math.round(ViewCompat.getTranslationX(child));
                        final int right1 = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child) + h_spacing);
                        final int top1 = child.getTop() + params.topMargin + Math.round(ViewCompat.getTranslationY(child)) - v_spacing;
                        final int bottom1 = top1 + v_spacing;

                        mDivider.setBounds(left1, top1, right1, bottom1);
                        mDivider.draw(c);
                    }
                }
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);

        HeaderAndFooterRecyclerViewAdapter mAdapter = (HeaderAndFooterRecyclerViewAdapter) parent.getAdapter();

        /**
         * header和footer
         */
        if (hasHeader) {

            if (position == 0) {
                return;
            } else {
                position = position - 1;
            }

            if (mAdapter.isFooter(position + 1)) {
                return;
            }
        } else {

            if (mAdapter.isFooter(position)) {
                return;
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
}