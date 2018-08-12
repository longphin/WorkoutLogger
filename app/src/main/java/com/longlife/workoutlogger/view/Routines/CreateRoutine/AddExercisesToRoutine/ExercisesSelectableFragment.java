package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
			ViewModelProviders.of(getActivity(), viewModelFactory)
				.get(ExercisesSelectableViewModel.class);
	}
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
}
