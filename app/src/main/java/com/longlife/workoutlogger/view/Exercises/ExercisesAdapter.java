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
	
	private IClickExercise exerciseClickCallback;
	
	public ExercisesAdapter(ExercisesViewModel viewModel, IClickExercise exerciseClickCallback)
	{
		this.viewModel = viewModel;
		this.exerciseClickCallback = exerciseClickCallback;
	}
	
	// Overrides
	@Override
	public void onBindViewHolder(ExercisesViewHolder holder, int pos)
	{
		final int position = holder.getAdapterPosition();
		Exercise ex = exercises.get(position);
		// Name
		StringBuilder sbName = new StringBuilder(100);
		sbName.append(ex.getName())
			.append(" (")
			.append(ex.getIdExercise())
			.append(" -> ")
			.append(ex.getCurrentIdExerciseHistory())
			.append(")");
		holder.setNameText(sbName.toString());
		// Description
		holder.setDescripText(ex.getDescription());
		// Favorite icon
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
		
		// Edit exercise
		holder.getNameTextView().setOnClickListener(view ->
		{
			exerciseClickCallback.exerciseClicked(ex.getIdExercise());
		});
	}
	
	// Setters
	@Override
	public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
		
		return (new ExercisesViewHolder(v));
	}
	
	public void exerciseUpdated(Exercise updatedExercise)
	{
		final Long idExerciseEdited = updatedExercise.getIdExercise();
		// Find where in the adapter this exercise is and notify the change.
		for(int i = 0; i < exercises.size(); i++){
			if(exercises.get(i).getIdExercise().equals(idExerciseEdited)){
				exercises.set(i, updatedExercise);
				notifyItemChanged(i);
				return;
			}
		}
	}
	
	@Override
	public int getItemCount()
	{
		return exercises.size();
	}
	
	// Interface for when an item is clicked. Should be implemented by the Activity to start an edit fragment.
	public interface IClickExercise
	{
		// When an exercise is clicked, send the clicked exercise.
		void exerciseClicked(Long idExercise);
	}
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
	
	public void restoreExercise(Exercise restoredItem, int restoredPosition)
	{
		exercises.add(restoredPosition, restoredItem);
		notifyItemInserted(restoredPosition);
	}
	
	public void addExercise(Exercise ex)
	{
		exercises.add(ex);
		notifyItemInserted(exercises.size() - 1);
	}
}
