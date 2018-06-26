package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter
	extends RecyclerView.Adapter<ExercisesViewHolder>
{
	private List<Exercise> exercises;
	private List<Boolean> selectedExercises = new ArrayList<>();
	private ExercisesOverviewViewModel viewModel;
	private int itemLayout;
	
	public ExercisesAdapter(ExercisesOverviewViewModel viewModel, int itemLayout)
	{
		this.viewModel = viewModel;
		this.itemLayout = itemLayout;
	}
	
	// Setters
	public void setExercises(List<Exercise> exercises)
	{
		if(exercises == null)
			return;
		
		this.exercises = exercises;
		//this.selectedExercises = new ArrayList<>(exercises.size());
		this.selectedExercises = new ArrayList<>();
		while(selectedExercises.size() < exercises.size())
			selectedExercises.add(false); //[TODO] This causes error. Need to set this as the same size as exercises.
		notifyDataSetChanged();
	}
	
	public void removeExercise(int position)
	{
		exercises.remove(position);
		selectedExercises.remove(position);
		notifyItemRemoved(position);
	}
	
	public void restoreExercise(Exercise ex, int position)
	{
		exercises.add(position, ex);
		selectedExercises.add(position, false);
		notifyItemInserted(position);
	}
	
	// Overrides
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
			selectedCheckBox.setOnClickListener(
				view ->
				{
					selectedExercises.set(position, holder.getSelectedCheckBox().isChecked());//!selectedExercises.get(position));
					Log.d("ExercisesAdapter", String.valueOf(position) + " is " + (selectedExercises.get(position) ? " selected" : " unselected"));
				}
			);
		}
	}
}
// Inner Classes
