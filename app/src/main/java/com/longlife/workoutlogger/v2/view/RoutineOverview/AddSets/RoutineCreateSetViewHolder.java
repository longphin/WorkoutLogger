package com.longlife.workoutlogger.v2.view.RoutineOverview.AddSets;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.RelativeLayout;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.utils.RecyclerViewHolderSwipeable;

public class RoutineCreateSetViewHolder
	extends RecyclerViewHolderSwipeable
{
	private RelativeLayout viewBackground;
	private ConstraintLayout viewForeground;
	
	public RoutineCreateSetViewHolder(View itemView)
	{
		super(itemView);
		
		this.viewBackground = itemView.findViewById(R.id.item_routine_create_exercise_set_background);
		this.viewForeground = itemView.findViewById(R.id.item_routine_create_exercise_set_foreground);
	}
	
	// Overrides
	@Override
	public RelativeLayout getViewBackground()
	{
		return viewBackground;
	}
	
	@Override
	public ConstraintLayout getViewForeground()
	{
		return viewForeground;
	}
}
