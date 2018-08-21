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
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesSelectableFragment
	extends ExercisesFragment
{
	public static final String TAG = ExercisesFragment.class.getSimpleName(); // The tag will be the same as the base class. //ExercisesSelectableFragment.class.getSimpleName();
	private ExercisesSelectableViewModel exercisesSelectableViewModel;
	
	// Overrides
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		exercisesSelectableViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesSelectableViewModel.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		Button addExercisesToRoutineButton = mView.findViewById(R.id.btn_routine_exercises_addExercisesToRoutine);
		addExercisesToRoutineButton.setOnClickListener(
			view ->
			{
				exercisesSelectableViewModel.addExercisesToRoutine();
				exercisesSelectableViewModel.clearIdSelectedExercises(); // Clear selected exercises, which is possibly when the selected exercises are clicked again.
				getActivity().onBackPressed();
			}
		);
		
		Button clearSelectionButton = mView.findViewById(R.id.btn_routine_exercises_clear);
		clearSelectionButton.setOnClickListener(
			view ->
			{
				exercisesSelectableViewModel.clearIdSelectedExercises();
				adapter.notifyDataSetChanged();
			}
		);
		
		return mView;
	}
	
	public static ExercisesSelectableFragment newInstance(ExercisesViewModel exercisesViewModel, ExercisesSelectableViewModel exercisesSelectableViewModel, int activityRoot, int exerciseItemLayout)
	{
		ExercisesSelectableFragment fragment = new ExercisesSelectableFragment();
		fragment.setAdapter(new ExercisesSelectableAdapter(exercisesViewModel, exercisesSelectableViewModel, fragment));
		
		Bundle bundle = new Bundle();
		bundle.putInt("activityRoot", activityRoot);
		bundle.putInt("exerciseItemLayout", exerciseItemLayout);
		fragment.setArguments(bundle);
		
		return fragment;
	}
}
