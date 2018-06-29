package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExercisesAdapter
	extends RecyclerView.Adapter<ExercisesViewHolder>
{
	// Static
	private static final String TAG = "ExercisesAdapter";
	private List<Exercise> exercises;
	private Set<Integer> selectedIdExercises = new HashSet<>();
	private ExercisesOverviewViewModel viewModel;
	private int itemLayout;
	
	public ExercisesAdapter(ExercisesOverviewViewModel viewModel, int itemLayout)
	{
		this.viewModel = viewModel;
		this.itemLayout = itemLayout;
	}
	// Overrides
	@Override
	public void onBindViewHolder(ExercisesViewHolder holder, int pos)
	{
		int position = holder.getAdapterPosition();
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
		
		CheckBox selectedCheckBox = holder.getSelectedCheckBox();
		if(selectedCheckBox != null){
			int idCurrentlySelected = ex.getIdExercise();
			boolean isCurrentlySelected = selectedIdExercises.contains(idCurrentlySelected);//viewModel.isIdSelected(idCurrentlySelected);//selectedIdExercises.contains(id);
			if(isCurrentlySelected){
				holder.setSelectedCheckBox(true);
				Log.d(TAG, "Is " + String.valueOf(idCurrentlySelected) + " selected: Yes");
			}else{
				holder.setSelectedCheckBox(false); // This can happen if the holder view was restored (such as when item was deleted, then undone).
				Log.d(TAG, "Is " + String.valueOf(idCurrentlySelected) + " selected: No");
			}
			
			selectedCheckBox.setOnClickListener(
				view ->
				{
					int id = ex.getIdExercise();
					boolean isSelected = selectedIdExercises.contains(id);//viewModel.isIdSelected(id);//selectedIdExercises.contains(id);
					if(isSelected){
						selectedIdExercises.remove(id);
						//viewModel.removeSelectedExercise(id);
					}else{
						selectedIdExercises.add(id);
						//viewModel.addSelectedExercise(id);
					}
					Log.d(TAG, String.valueOf(id) + " is " + (!isSelected ? "selected" : "unselected"));
				}
			);
		}
	}
	
	public void setExercises(List<Exercise> exercises)
	{
		if(exercises == null)
			return;
		
		this.exercises = exercises;
		notifyDataSetChanged();
	}
	
	// Getters
	// Convert the Set for selected id exercises to be added to a routine into a List.
	/*
	public List<Integer> getSelectedIdExercisesList()
	{
		return new ArrayList<>(selectedIdExercises);
	}
	*/
	public Set<Integer> getSelectedIdExercisesList()
	{
		return selectedIdExercises;
	}
	
	// Setters
	
	// "Undo" the temporary delete of an exercise.
	public void restoreExercise(Exercise ex, int position)
	{
		exercises.add(position, ex);
		notifyItemInserted(position);
	}
	
	// This is a temporary delete (only deletes the exercise from memory). After the "Undo" snackbar
	// expires, the exercise then is deleted from the database.
	public void removeExercise(int position)
	{
		//selectedIdExercises.remove(exercises.get(position).getIdExercise());
		exercises.remove(position);
		notifyItemRemoved(position);
	}
	
	@Override
	public int getItemCount()
	{
		if(exercises == null)
			return 0;
		return exercises.size();
	}
	
	@Override
	public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
		
		return (new ExercisesViewHolder(v));
	}
	
	// This is called when the exercise has been removed from the database.
	public void permanentlyRemoveExercise(int idExercise)
	{
		selectedIdExercises.remove(idExercise);
	}
}
