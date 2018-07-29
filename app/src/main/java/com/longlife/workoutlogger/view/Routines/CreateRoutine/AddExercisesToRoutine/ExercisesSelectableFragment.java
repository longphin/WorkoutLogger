package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.view.Exercises.ExercisesFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesSelectableFragment
	extends ExercisesFragment
{
	public static final String TAG = ExercisesSelectableFragment.class.getSimpleName();
	private ExercisesSelectableViewModel viewModel;
	
	// Overrides
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		Button addExercisesToRoutineButton = mView.findViewById(R.id.btn_routine_exercises_addExercisesToRoutine);
		addExercisesToRoutineButton.setOnClickListener(
			view ->
			{
				viewModel.addExercisesToRoutine();
				viewModel.clearIdSelectedExercises(); // Clear selected exercises, which is possibly when the selected exercises are clicked again.
				getActivity().onBackPressed();
			}
		);
		
		Button clearSelectionButton = mView.findViewById(R.id.btn_routine_exercises_clear);
		clearSelectionButton.setOnClickListener(
			view ->
			{
				viewModel.clearIdSelectedExercises();
				adapter.notifyDataSetChanged();
			}
		);
		
		return mView;
	}
	
	// Setters
	public void setViewModel(ExercisesSelectableViewModel viewModel)
	{
		this.viewModel = viewModel;
	}
}
