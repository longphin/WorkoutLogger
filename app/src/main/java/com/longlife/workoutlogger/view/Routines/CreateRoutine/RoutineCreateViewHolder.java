package com.longlife.workoutlogger.view.Routines.CreateRoutine;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.R;

public class RoutineCreateViewHolder
	extends RecyclerViewHolderSwipeable//RecyclerView.ViewHolder
{
	private TextView name;
	private RelativeLayout viewBackground;
	private ConstraintLayout viewForeground;
	private ImageView upButton;
	private ImageView downButton;
	
	public RoutineCreateViewHolder(View itemView)
	{
		super(itemView);
		
		this.name = itemView.findViewById(R.id.txt_routinecreate_exerciseName);
		this.viewForeground = itemView.findViewById(R.id.foreground_routine_create_exercise);
		this.viewBackground = itemView.findViewById(R.id.background_routine_create_exercise);
		this.upButton = itemView.findViewById(R.id.imv_moveExerciseUp);
		this.downButton = itemView.findViewById(R.id.imv_moveExerciseDown);
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
	public ImageView getDownButton(){return downButton;}
	
	public TextView getNameTextView()
	{
		return name;
	}
	
	public ImageView getUpButton(){return upButton;}
	
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
