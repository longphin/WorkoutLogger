package com.longlife.workoutlogger.view.Exercises;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.R;

public class ExercisesViewHolder
	extends RecyclerViewHolderSwipeable
{
	// Overrides
	
	private TextView name;
	private TextView descrip;
	private RelativeLayout viewBackground;
	private ConstraintLayout viewForeground;
	private CheckBox selectedCheckBox;
	private ImageView favoriteIcon;
	
	public ExercisesViewHolder(View itemView)
	{
		super(itemView);
		
		this.name = itemView.findViewById(R.id.txt_exerciseName);
		this.descrip = itemView.findViewById(R.id.txt_exerciseDescrip);
		this.viewBackground = itemView.findViewById(R.id.background_exercise_item);
		this.viewForeground = itemView.findViewById(R.id.foreground_exercise_item);
		this.favoriteIcon = itemView.findViewById(R.id.icon_exercise_favorite);
		this.selectedCheckBox = itemView.findViewById(R.id.ch_selectExercise);
	}
	
	// Getters
	public RelativeLayout getViewBackground()
	{
		return viewBackground;
	}
	
	public ConstraintLayout getViewForeground()
	{
		return viewForeground;
	}
	
	public TextView getNameTextView()
	{
		return name;
	}
	public ImageView getFavoriteIcon(){ return favoriteIcon;}
	
	public CheckBox getSelectedCheckBox()
	{
		return selectedCheckBox;
	}
	
	// Setters
	public void setFavoriteIcon(int icon)
	{
		favoriteIcon.setImageResource(icon);
	}
	
	public void setNameText(String s)
	{
		this.name.setText(s);
	}
	
	public void setDescripText(String s)
	{
		this.descrip.setText(s);
	}
	
	public void setSelectedCheckBox(Boolean b)
	{
		this.selectedCheckBox.setChecked(b);
	}
}
// Inner Classes
