package com.longlife.workoutlogger.view.Exercises;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter
	extends RecyclerView.Adapter<ExercisesViewHolder>
{
	private ExercisesViewModel viewModel;
	protected List<Exercise> exercises = new ArrayList<>();
	
	public ExercisesAdapter(ExercisesViewModel viewModel)
	{
		this.viewModel = viewModel;
	}
	
	// Overrides
	@Override
	public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
		
		return (new ExercisesViewHolder(v));
	}
	
	@Override
	public void onBindViewHolder(ExercisesViewHolder holder, int pos)
	{
		final int position = holder.getAdapterPosition();
		Exercise ex = exercises.get(position);
		
		StringBuilder sbName = new StringBuilder(100);
		sbName.append(ex.getName())
			.append(" (")
			.append(ex.getIdExercise())
			.append(")");
		
		holder.setNameText(sbName.toString());
		holder.setDescripText(ex.getDescription());
		
		if(ex.getFavorited()){
			holder.setFavoriteIcon(R.drawable.ic_favorite_black_24dp);
		}else{
			holder.setFavoriteIcon(R.drawable.ic_favorite_border_black_24dp);
		}
		
		holder.getFavoriteIcon().setOnClickListener(view ->
			{
				ex.setFavorited(!ex.getFavorited());
				if(ex.getFavorited()){
					holder.setFavoriteIcon(R.drawable.ic_favorite_black_24dp);
				}else{
					holder.setFavoriteIcon(R.drawable.ic_favorite_border_black_24dp);
				}
				
				viewModel.updateFavorite(ex.getIdExercise(), ex.getFavorited());
			}
		);
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
		notifyDataSetChanged();
	}
	
	public Exercise getExercise(int position)
	{
		return exercises.get(position);
	}
	
	public void removeExercise(int position)
	{
		exercises.remove(position);
		notifyItemRemoved(position);
	}
	
	public void restoreExercise(Exercise deletedItem, int deletedIndex)
	{
		exercises.add(deletedIndex, deletedItem);
		notifyItemInserted(deletedIndex);
	}
	
	public void addExercise(Exercise ex)
	{
		exercises.add(ex);
		notifyItemInserted(exercises.size() - 1);
	}
}
