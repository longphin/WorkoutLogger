package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.view.Exercises.ExercisesAdapter;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewHolder;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;

public class ExercisesSelectableAdapter
	extends ExercisesAdapter
{
	// Static
	private final static String TAG = ExercisesSelectableAdapter.class.getSimpleName();
	
	private ExercisesSelectableViewModel viewModel;
	
	public ExercisesSelectableAdapter(ExercisesViewModel exercisesViewModel, ExercisesSelectableViewModel viewModel, IClickExercise clickExerciseCallback)
	{
		super(exercisesViewModel, clickExerciseCallback);
		this.viewModel = viewModel;
	}
	
	// Overrides
	@Override
	public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_selectable, parent, false);
		
		return (new ExercisesViewHolder(v));
	}
	
	@Override
	public void onBindViewHolder(ExercisesViewHolder holder, int pos)
	{
		super.onBindViewHolder(holder, pos);
		
		final int position = holder.getAdapterPosition();
		final Exercise ex = exercises.get(position);
		
		CheckBox selectedCheckBox = holder.getSelectedCheckBox();
		if(selectedCheckBox != null){
			Long idCurrentlySelected = ex.getIdExercise();
			boolean isCurrentlySelected = viewModel.isIdSelected(idCurrentlySelected);
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
					Long id = ex.getIdExercise();
					boolean isSelected = viewModel.isIdSelected(id);
					if(isSelected){
						//selectedIdExercises.remove(id);
						viewModel.removeSelectedExercise(id);
					}else{
						//selectedIdExercises.add(id);
						viewModel.addSelectedExercise(id);
					}
					Log.d(TAG, String.valueOf(id) + " is " + (!isSelected ? "selected" : "unselected"));
				}
			);
		}
		
		CheckBox exerciseSelectedBox = holder.getSelectedCheckBox();
		exerciseSelectedBox.setOnClickListener(view -> {
			final int thisPos = holder.getAdapterPosition();
			viewModel.addSelectedExercise(exercises.get(thisPos).getIdExercise());
			//selectedIdExercises.add(exercises.get(thisPos).getIdRoutineHistory());
		});
	}
	
}
