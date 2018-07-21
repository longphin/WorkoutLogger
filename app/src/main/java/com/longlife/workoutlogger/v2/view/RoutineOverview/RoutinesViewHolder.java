package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

public class RoutinesViewHolder
	extends RecyclerView.ViewHolder
{
	private TextView name;
	private TextView descrip;
	
	public RoutinesViewHolder(View itemView)
	{
		super(itemView);
		
		this.name = itemView.findViewById(R.id.txt_routineName);
		this.descrip = itemView.findViewById(R.id.txt_routineDescrip);
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
