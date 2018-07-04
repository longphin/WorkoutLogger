package com.longlife.workoutlogger.v2.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

// Swipe listener for recyclerview. The recyclerview must extend RecyclerViewHolderSwipeable.
public class RecyclerItemTouchHelper
	extends ItemTouchHelper.SimpleCallback
{
	private RecyclerItemTouchHelperListener listener;
	
	public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener)
	{
		super(dragDirs, swipeDirs);
		this.listener = listener;
	}
	
	// Overrides
	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
	{
		return listener.onMove(recyclerView, viewHolder, target);
	}
	
	@Override
	public boolean isLongPressDragEnabled()
	{
		return listener.isLongPressDragEnabled();
	}
	
	@Override
	public boolean isItemViewSwipeEnabled()
	{
		return listener.isItemViewSwipeEnabled();
	}
	
	@Override
	public void onSelectedChanged(RecyclerView.ViewHolder viewHolder
		, int actionState)
	{
		super.onSelectedChanged(viewHolder, actionState);
		
		/*
		if(viewHolder != null){
			// If the item is being moved, then hide the background images.
			if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
				final View backgroundView = ((RecyclerViewHolderSwipeable)viewHolder).getViewBackground();
				final ImageView backgroundDeleteIcon = ((RecyclerViewHolderSwipeable)viewHolder).getDeleteIcon();
				
				if(backgroundView != null)
					backgroundView.getBackground().setAlpha(0);
				if(backgroundDeleteIcon != null)
					backgroundDeleteIcon.setImageAlpha(0);
			}
			
			//getDefaultUIUtil().onSelected(foregroundView);
			super.onSelectedChanged(viewHolder, actionState);
		}
		*/
	}
	
	@Override
	public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
		RecyclerView.ViewHolder viewHolder, float dX, float dY,
		int actionState, boolean isCurrentlyActive)
	{
		/*
		final View foregroundView = ((RecyclerViewHolderSwipeable)viewHolder).getViewForeground();
		
		getDefaultUIUtil().onDrawOver(c, recyclerView,
			foregroundView,
			dX, dY,
			actionState, isCurrentlyActive
		);
		*/
		super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
	}
	
	@Override
	public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
	{
		final View foregroundView = ((RecyclerViewHolderSwipeable)viewHolder).getViewForeground();
		/*
		final View backgroundView = ((RecyclerViewHolderSwipeable)viewHolder).getViewBackground();
		final ImageView backgroundDeleteIcon = ((RecyclerViewHolderSwipeable)viewHolder).getDeleteIcon();
		
		if(backgroundView != null)
			backgroundView.getBackground().setAlpha(255);
		if(backgroundDeleteIcon != null)
			backgroundDeleteIcon.setImageAlpha(255);
		*/
		getDefaultUIUtil().clearView(foregroundView);
		
		super.clearView(recyclerView, viewHolder);
	}
	
	@Override
	public void onChildDraw(Canvas c, RecyclerView recyclerView,
		RecyclerView.ViewHolder viewHolder, float dX, float dY,
		int actionState, boolean isCurrentlyActive)
	{
		// If swiping, then only move the foreground.
		if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
			if(viewHolder != null){
				final View foregroundView = ((RecyclerViewHolderSwipeable)viewHolder).getViewForeground();
				
				getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
					actionState, isCurrentlyActive
				);
			}
		}else{
			super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
		}
	}
	
	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
	{
		listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
	}
	
	@Override
	public int convertToAbsoluteDirection(int flags, int layoutDirection)
	{
		return super.convertToAbsoluteDirection(flags, layoutDirection);
	}
	
	// Inner Classes
	public interface RecyclerItemTouchHelperListener
	{
		// Getters
		boolean isItemViewSwipeEnabled();
		
		boolean isLongPressDragEnabled();

		void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
		
		boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);
	}
}
// Inner Classes
