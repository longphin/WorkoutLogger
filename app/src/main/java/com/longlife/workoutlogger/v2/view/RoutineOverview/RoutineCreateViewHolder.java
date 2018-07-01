package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.utils.RecyclerViewHolderSwipeable;

public class RoutineCreateViewHolder
	extends RecyclerViewHolderSwipeable//RecyclerView.ViewHolder
{
	private TextView name;
	private RelativeLayout viewBackground;
	private ConstraintLayout viewForeground;
	private TextView descrip;
	
	public RoutineCreateViewHolder(View itemView)
	{
		super(itemView);
		
		this.name = itemView.findViewById(R.id.txt_routinecreate_exerciseName);
		this.viewForeground = itemView.findViewById(R.id.foreground_routine_create_exercise);
		this.viewBackground = itemView.findViewById(R.id.background_routine_create_exercise);
		this.descrip = itemView.findViewById(R.id.txt_routinecreate_exerciseDescrip);
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
	
	// Getters
	public TextView getDescripTextView(){return descrip;}

	// Setters
	public void setNameText(String s)
	{
		this.name.setText(s);
	}
	/*
	public void setDescripText(String s)
	{
		this.descrip.setText(s);
	}*/
}
// Inner Classes
