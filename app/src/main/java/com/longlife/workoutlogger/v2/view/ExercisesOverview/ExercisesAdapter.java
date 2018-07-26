package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.comparators.ExerciseComparators;
import com.longlife.workoutlogger.v2.utils.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ExercisesAdapter
	extends RecyclerView.Adapter<ExercisesViewHolder>
{
	// Static
	private static final String TAG = ExercisesAdapter.class.getSimpleName();
	private List<Exercise> exercises = new ArrayList<>();
	private Set<Integer> selectedIdExercises = new HashSet<>();
	private ExercisesOverviewViewModel viewModel;
	private int itemLayout;
	private CompositeDisposable composite = new CompositeDisposable();
	
	public ExercisesAdapter(ExercisesOverviewViewModel viewModel, int itemLayout)
	{
		this.viewModel = viewModel;
		this.itemLayout = itemLayout;
		
		Log.d(TAG, "Constructor");
		// Observe events when the list of exercises is obtained.
		//addDisposable(viewModel.getLoadResponse().subscribe(response -> processLoadResponse(response)));
		//addDisposable(viewModel.getInsertResponse().subscribe(response -> processInsertResponse(response)));
		
		viewModel.loadExercises();
	}
	
	// Overrides
	// Getters
	// Setters
	public void setExercises(List<Exercise> exercises)
	{
		this.exercises = exercises;
		notifyDataSetChanged();
	}
	
	// Methods
	private void processLoadResponse(Response<List<Exercise>> response)
	{
		switch(response.getStatus()){
			case LOADING:
				renderLoadingState();
				break;
			case SUCCESS:
				renderSuccessState(response.getValue());
				break;
			case ERROR:
				renderErrorState(response.getError());
				break;
		}
	}
	
	// Insertion Response
	private void processInsertResponse(Response<ExerciseInsertHelper> response)
	{
		switch(response.getStatus()){
			case LOADING:
				renderInsertLoadingState();
				break;
			case SUCCESS:
				renderInsertSuccessState(response.getValue());
				break;
			case ERROR:
				renderInsertErrorState(response.getError());
				break;
		}
	}
	
	private void renderLoadingState()
	{
		Log.d(TAG, "loading exercises");
	}
	
	private void renderSuccessState(List<Exercise> exercises)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(exercises == null ? 0 : exercises.size());
		sb.append(" exercises obtained");
		
		Log.d(TAG, sb.toString());
		
		if(exercises == null)
			return;
		
		this.exercises = exercises;
		notifyDataSetChanged();
	}
	
	private void renderErrorState(Throwable throwable)
	{
		// change anything if loading data had an error.
		Log.d(TAG, throwable.getMessage());
	}
	
	private void renderInsertLoadingState()
	{
	}
	
	private void renderInsertSuccessState(ExerciseInsertHelper exerciseInsertHelper)
	{
		Collections.sort(this.exercises, ExerciseComparators.getDefaultComparator());
		for(int i = 0; i < this.exercises.size(); i++){
			if(exercises.get(i).getIdExercise() == exerciseInsertHelper.getExercise().getIdExercise()){
				exerciseInsertHelper.setInsertPosition(i);
				break;
			}
		}
		
		exercises.add(exerciseInsertHelper.getInsertPosition(), exerciseInsertHelper.getExercise());
		notifyItemInserted(exerciseInsertHelper.getInsertPosition());
		//notifyDataSetChanged();
		//setExercises(viewModel.getCachedExercises());
	}
	
	private void renderInsertErrorState(Throwable throwable)
	{
		// change anything if loading data had an error.
		Log.d(TAG, throwable.getMessage());
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
	
	public void addDisposable(Disposable d)
	{
		composite.add(d);
	}
	public Set<Integer> getSelectedIdExercisesList()
	{
		return selectedIdExercises;
	}
	
	public void clearDisposables()
	{
		composite.clear();
	}
	
	public Exercise getExercise(int position)
	{
		return exercises.get(position);
	}
	
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
	
	// This is called when the exercise has been removed from the database.
	public void permanentlyRemoveExercise(int idExercise)
	{
		selectedIdExercises.remove(idExercise);
	}
	
	public void addExercise(ExerciseInsertHelper exerciseInsertHelper)
	{
		/*
		Collections.sort(this.exercises, ExerciseComparators.getDefaultComparator());
		for(int i = 0; i < this.exercises.size(); i++){
			if(exercises.get(i).getIdExercise() == exerciseInsertHelper.getExercise().getIdExercise()){
				exerciseInsertHelper.setInsertPosition(i);
				break;
			}
		}
		*/
		/*
		int insertPosition = 0;
		final String name = exerciseInsertHelper.getExercise().getName();
		for(Exercise ex : this.exercises)
		{
			if(name.compareToIgnoreCase(ex.getName())>0)
			{
			
			}
		}
		for(int i=0; i<this.exercises.size(); i++)
		{
			if(exerciseInsertHelper.getExercise().getName().compareToIgnoreCase(this.exercises.get(i).getName()) > 0)
			{
			
			}
		}
		
		exercises.add(exerciseInsertHelper.getInsertPosition(), exerciseInsertHelper.getExercise());
		notifyItemInserted(exerciseInsertHelper.getInsertPosition());
		*/
		final int insertedPosition = this.exercises.size();
		exercises.add(exerciseInsertHelper.getExercise());
		notifyItemInserted(insertedPosition);
	}
}
