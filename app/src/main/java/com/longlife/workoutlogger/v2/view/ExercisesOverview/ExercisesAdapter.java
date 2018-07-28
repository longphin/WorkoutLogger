package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter
	extends RecyclerView.Adapter<ExercisesViewHolder>
{
	private List<Exercise> exercises = new ArrayList<>();
	
	// Overrides
	@Override
	public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercises, parent, false);
		
		return (new ExercisesViewHolder(v));
	}
	
	@Override
	public void onBindViewHolder(ExercisesViewHolder holder, int position)
	{
		Exercise ex = exercises.get(position);
		
		StringBuilder sbName = new StringBuilder(100);
		sbName.append(ex.getName())
			.append(" (")
			.append(ex.getIdExercise())
			.append(")");
		
		holder.setNameText(sbName.toString());
		holder.setDescripText(ex.getDescription());
	}
	
	@Override
	public int getItemCount()
	{
		return exercises.size();
	}
	
	// Setters
	public void setExercises(List<Exercise> exercises)
	{
		this.exercises = exercises;
	}
	
	public Exercise getExercise(int position)
	{
		return exercises.get(position);
	}
	
	public void removeExercise(int position)
	{
		notifyItemRemoved(position);
		exercises.remove(position);
	}
	
	public void restoreExercise(Exercise deletedItem, int deletedIndex)
	{
		exercises.add(deletedIndex, deletedItem);
		notifyItemInserted(deletedIndex);
	}
}
