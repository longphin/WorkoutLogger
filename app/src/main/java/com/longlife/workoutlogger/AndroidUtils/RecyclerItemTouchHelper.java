/*
 * Created by Longphi Nguyen on 12/11/18 8:26 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/28/18 6:52 PM.
 */

package com.longlife.workoutlogger.AndroidUtils;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

// Swipe listener for recyclerview. The recyclerview must extend RecyclerViewHolderSwipeable.
public class RecyclerItemTouchHelper
        extends ItemTouchHelper.SimpleCallback {
    // The listener.
    private RecyclerItemTouchHelperListener listener;

    // Constructor.
    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    // On drag item event.
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return listener.onMove(recyclerView, viewHolder, target);
    }

    @Override
    // Determines if long pressing is enabled, such as for when dragging items.
    public boolean isLongPressDragEnabled() {
        return listener.isLongPressDragEnabled();
    }

    @Override
    // Determines if swiping is enabled.
    public boolean isItemViewSwipeEnabled() {
        return listener.isItemViewSwipeEnabled();
    }

    @Override
    // Event for when item is swiped.
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder
            , int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // If the item was swiped, then we need to clear out the foreground.
        if (viewHolder instanceof RecyclerViewHolderSwipeable) {
            final View foregroundView = ((RecyclerViewHolderSwipeable) viewHolder).getViewForeground();

            getDefaultUIUtil().clearView(foregroundView);
        }

        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        // If swiping, then only move the foreground.
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (viewHolder instanceof RecyclerViewHolderSwipeable) {
                final View foregroundView = ((RecyclerViewHolderSwipeable) viewHolder).getViewForeground();

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive
                );
            }
        } else { // Else, move the entire view.
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return listener.getSwipeDirs(recyclerView, viewHolder);
    }

    // Interface to talk to activity/fragment when an item is dragged or swiped.
    public interface RecyclerItemTouchHelperListener {
        // Determine whether the view is swipeable.
        boolean isItemViewSwipeEnabled();

        // Determine whether the view is draggable.
        boolean isLongPressDragEnabled();

        // Callback when view is swiped.
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

        // Callback when view is moved.
        boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);

        // Get valid directions that view can be moved in.
        int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    }
}

