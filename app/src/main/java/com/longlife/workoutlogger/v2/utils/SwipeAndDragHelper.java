package com.longlife.workoutlogger.v2.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

// Swipe and Drag for recycler view items.
// Reference: srijith@therubberduckdev.wordpress.com https://therubberduckdev.wordpress.com/2017/10/24/android-recyclerview-drag-and-drop-and-swipe-to-dismiss/
public class SwipeAndDragHelper
	extends ItemTouchHelper.Callback
{
	// Interface contract between adapter and touch helper.
	private ActionCompletionContract contract;
	
	public SwipeAndDragHelper(ActionCompletionContract contract)
	{
		this.contract = contract;
	}
	
	// Overrides
	@Override
	public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
	{
		int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
		int swipeFlags = ItemTouchHelper.RIGHT;
		/*
		if(!(viewHolder instanceof RecyclerViewHolderSwipeable))
		{
			return 0;
		}
		*/
		if(!contract.isSwipeable(viewHolder.getAdapterPosition()) && contract.isDraggable(viewHolder.getAdapterPosition())){
			return makeMovementFlags(dragFlags, 0);
		}
		if(contract.isSwipeable(viewHolder.getAdapterPosition()) && !contract.isDraggable(viewHolder.getAdapterPosition())){
			return makeMovementFlags(0, swipeFlags);
		}
		
		return makeMovementFlags(dragFlags, swipeFlags);
	}
	
	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
	{
		contract.onViewMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
		return true;
	}
	
	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
	{
		contract.onViewSwiped(viewHolder.getAdapterPosition());
	}
	
	@Override
	public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
	{
		if(viewHolder instanceof RecyclerViewHolderSwipeable){
			final View foregroundView = ((RecyclerViewHolderSwipeable)viewHolder).getViewForeground();
			getDefaultUIUtil().clearView(foregroundView);
		}
		
		super.clearView(recyclerView, viewHolder);
	}
	
	@Override
	public boolean isLongPressDragEnabled()
	{
		return false;
	}
	
	@Override
	public void onChildDraw(Canvas c,
		RecyclerView recyclerView,
		RecyclerView.ViewHolder viewHolder,
		float dX,
		float dY,
		int actionState,
		boolean isCurrentlyActive)
	{
		// If swiping, then only move the foreground.
		if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
			if(viewHolder != null && viewHolder instanceof RecyclerViewHolderSwipeable){
				/*
				float alpha = 1 - (Math.abs(dX) / recyclerView.getWidth());
				viewHolder.itemView.setAlpha(alpha);
				*/
				final View foregroundView = ((RecyclerViewHolderSwipeable)viewHolder).getViewForeground();
				getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
					actionState, isCurrentlyActive
				);
			}
		}else{
			super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
		}
	}
	
	public interface ActionCompletionContract
	{
		void onViewMoved(int oldPosition, int newPosition);
		
		void onViewSwiped(int position);
		
		boolean isSwipeable(int position);
		
		boolean isDraggable(int position);
	}
}
