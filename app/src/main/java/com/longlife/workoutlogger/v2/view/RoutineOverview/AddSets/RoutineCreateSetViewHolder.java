package com.longlife.workoutlogger.v2.view.RoutineOverview.AddSets;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.RelativeLayout;

import com.longlife.workoutlogger.v2.utils.RecyclerViewHolderSwipeable;

public class RoutineCreateSetViewHolder
	extends RecyclerViewHolderSwipeable
{
	public RoutineCreateSetViewHolder(View itemView)
	{
		super(itemView);
	}
	
	// Overrides
	@Override
	public RelativeLayout getViewBackground()
	{
		return null;
	}
	
	@Override
	public ConstraintLayout getViewForeground()
	{
		return null;
	}
}
