package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.utils.RecyclerViewHolderSwipeable;

public class RoutineCreateViewHolder
	extends RecyclerViewHolderSwipeable//RecyclerView.ViewHolder
{
	// Overrides
	
	private TextView name;
	private RelativeLayout viewBackground;
	private ConstraintLayout viewForeground;
	private TextView descrip;
	private ImageView upButton;
	private ImageView downButton;
	
	public RoutineCreateViewHolder(View itemView)
	{
		super(itemView);
		
		this.name = itemView.findViewById(R.id.txt_routinecreate_exerciseName);
		this.viewForeground = itemView.findViewById(R.id.foreground_routine_create_exercise);
		this.viewBackground = itemView.findViewById(R.id.background_routine_create_exercise);
		this.descrip = itemView.findViewById(R.id.txt_routinecreate_exerciseDescrip);
		this.upButton = itemView.findViewById(R.id.imv_moveExerciseUp);
		this.downButton = itemView.findViewById(R.id.imv_moveExerciseDown);
	}
	
	// Getters
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
	
	public TextView getNameTextView()
	{
		return name;
	}
	public ImageView getDownButton(){return downButton;}
	
	public ImageView getUpButton(){return upButton;}
	
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
