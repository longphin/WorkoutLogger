package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class RoutineCreateAdapter
	extends RecyclerView.Adapter<RoutineCreateViewHolder>
{
	private List<Exercise> exercisesToInclude = new ArrayList<>();
	
	private Context context;
	
	public void addExercise(Exercise ex)
	{
		exercisesToInclude.add(ex);
	}
	
	// Overrides
	@Override
	public RoutineCreateViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		this.context = parent.getContext();
		
		View v = LayoutInflater.from(this.context).inflate(R.layout.item_routine_create_exercise, parent, false);
		
		return new RoutineCreateViewHolder(v);
	}
	
	@Override
	public void onBindViewHolder(RoutineCreateViewHolder holder, int pos)
	{
		int position = holder.getAdapterPosition();
		Exercise exercise = exercisesToInclude.get(position);
		
		StringBuilder sbName = new StringBuilder(100);
		sbName.append(exercise.getName())
			.append(" (")
			.append(exercise.getIdExercise())
			.append(")");
		
		holder.setNameText(sbName.toString());
		//holder.setDescripText(exercise.getDescription());
	}
	
	// Getters
	public List<Exercise> getExercises(){return exercisesToInclude;}
	
	public void addExercises(List<Exercise> ex)
	{
		exercisesToInclude.addAll(ex);
	}
	
	public void removeExerciseAtPosition(int pos)
	{
		try{
			exercisesToInclude.remove(pos);
		}catch(Exception e){
		}
	}
	
	// "Undo" the temporary delete of an exercise.
	public void restoreExercise(Exercise ex, int position)
	{
		exercisesToInclude.add(position, ex);
		notifyItemInserted(position);
	}
	
	@Override
	public int getItemCount()
	{
		return exercisesToInclude.size();
	}
}
// Inner Classes
