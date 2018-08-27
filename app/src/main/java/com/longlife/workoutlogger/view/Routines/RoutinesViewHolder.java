package com.longlife.workoutlogger.view.Routines;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.R;

public class RoutinesViewHolder
	extends RecyclerViewHolderSwipeable
{
	private TextView name;
	private TextView descrip;
	private RelativeLayout background;
	private ConstraintLayout foreground;
	
	public RoutinesViewHolder(View itemView)
	{
		super(itemView);
		
		this.name = itemView.findViewById(R.id.txt_routineName);
		this.descrip = itemView.findViewById(R.id.txt_routineDescrip);
		this.background = itemView.findViewById(R.id.background_routine_item);
		this.foreground = itemView.findViewById(R.id.foreground_routine_item);
	}
	
	// Overrides
	@Override
	public RelativeLayout getViewBackground()
	{
		return background;
	}
	
	@Override
	public ConstraintLayout getViewForeground()
	{
		return foreground;
	}
	
	// Getters
	public TextView getNameTextView()
	{
		return name;
	}
	
	// Setters
	public void setNameText(String s)
	{
		this.name.setText(s);
	}
	
	public void setDescripText(String s)
	{
		this.descrip.setText(s);
	}
}
// Inner Classes
