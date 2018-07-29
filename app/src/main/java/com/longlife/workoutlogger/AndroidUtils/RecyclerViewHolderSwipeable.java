package com.longlife.workoutlogger.AndroidUtils;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class RecyclerViewHolderSwipeable
	extends RecyclerView.ViewHolder
{
	public RecyclerViewHolderSwipeable(View itemView)
	{
		super(itemView);
	}
	
	// Getters
	public abstract RelativeLayout getViewBackground();
	
	public abstract ConstraintLayout getViewForeground();
}
