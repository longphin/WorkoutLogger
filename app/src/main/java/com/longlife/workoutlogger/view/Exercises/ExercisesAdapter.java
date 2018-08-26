package com.longlife.workoutlogger.view.Exercises;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.view.Exercises.Helper.ExerciseFavorited;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter
	extends RecyclerView.Adapter<ExercisesViewHolder>
{
	protected List<Exercise> exercises = new ArrayList<>();
	
	private IClickExercise exerciseClickCallback;
	
	public ExercisesAdapter(IClickExercise exerciseClickCallback)
	{
		this.exerciseClickCallback = exerciseClickCallback;
	}
	
	// Overrides
	@Override
	public void onBindViewHolder(ExercisesViewHolder holder, int pos)
	{
		final int position = holder.getAdapterPosition();
		Exercise ex = exercises.get(position);
		// Name
		holder.setNameText(ex.getName() + " (" + String.valueOf(ex.getIdExercise()) + " -> " + String.valueOf(ex.getCurrentIdExerciseHistory()) + ")");
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
/*				ex.setFavorited(!ex.getFavorited());
				if(ex.getFavorited()){
					holder.setFavoriteIcon(R.drawable.ic_favorite_black_24dp);
				}else{
					holder.setFavoriteIcon(R.drawable.ic_favorite_border_black_24dp);
				}*/
				
				//viewModel.updateFavorite(ex.getIdExercise(), ex.getFavorited());
				exerciseClickCallback.exerciseFavorited(ex.getIdExercise(), !ex.getFavorited());
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
	
	public void exerciseFavorited(ExerciseFavorited exerciseFavorited)
	{
		final Long idExercise = exerciseFavorited.getIdExercise();
		final boolean favoritedStatus = exerciseFavorited.isFavorited();
		
		for(int i = 0; i < exercises.size(); i++){
			if(exercises.get(i).getIdExercise().equals(idExercise)){
				exercises.get(i).setFavorited(favoritedStatus);
				notifyItemChanged(i);
			}
		}
	}
	
	// Interface for when an item is clicked. Should be implemented by the Activity/Fragment to start an edit fragment.
	public interface IClickExercise
	{
		// When an exercise is clicked, send the clicked exercise.
		void exerciseClicked(Long idExercise);
		
		void exerciseFavorited(Long idExercise, boolean favoritedStatus);
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
