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
import com.longlife.workoutlogger.view.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesSelectableFragment
	extends ExercisesFragment
	implements ExercisesSelectableAdapter.IExercisesSelectableAdapterCallback
{
	public static final String TAG = ExercisesFragment.class.getSimpleName(); // The tag will be the same as the base class. //ExercisesSelectableFragment.class.getSimpleName();
	private ExercisesSelectableViewModel exercisesSelectableViewModel;
	
	// Overrides
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setAdapter(new ExercisesSelectableAdapter(getActivity(), this, this));
		
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
		
		((MainActivity)getActivity()).updateToolbarTitle(getString(R.string.Toolbar_RoutineCreate));
		return mView;
	}
	
	@Override
	public boolean isIdSelected(Long idExercise)
	{
		return exercisesSelectableViewModel.isIdSelected(idExercise);
	}
	
	@Override
	public void removeSelectedExcercise(Long idExercise)
	{
		exercisesSelectableViewModel.removeSelectedExercise(idExercise);
	}
	
	@Override
	public void addSelectedExercise(Long idExercise)
	{
		exercisesSelectableViewModel.addSelectedExercise(idExercise);
	}
	
	public static ExercisesSelectableFragment newInstance(ExercisesSelectableViewModel exercisesSelectableViewModel, int activityRoot, int exerciseItemLayout)
	{
		ExercisesSelectableFragment fragment = new ExercisesSelectableFragment();
		//fragment.setAdapter(new ExercisesSelectableAdapter(fragment, fragment));
		
		Bundle bundle = new Bundle();
		bundle.putInt("activityRoot", activityRoot);
		bundle.putInt("exerciseItemLayout", exerciseItemLayout);
		fragment.setArguments(bundle);
		
		return fragment;
	}
}
