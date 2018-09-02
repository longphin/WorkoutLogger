package com.longlife.workoutlogger.view.Exercises.PerformExercise;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.view.MainActivity;
import com.longlife.workoutlogger.view.Perform.PerformFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerformExerciseFragment
	extends FragmentBase
{
	public static final String TAG = PerformFragment.TAG;
	private Long idExercise;
	
	public PerformExerciseFragment()
	{
		// Required empty public constructor
	}
	
	// Overrides
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		idExercise = getArguments().getLong("idExercise");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		((MainActivity)getActivity()).updateToolbarTitle(getString(R.string.Toolbar_PerformExercise, getArguments().getString("exerciseName")));
		
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_perform_exercise, container, false);
	}
	
	public static PerformExerciseFragment newInstance(Long idExercise, String exerciseName)
	{
		Bundle bundle = new Bundle();
		bundle.putLong("idExercise", idExercise);
		bundle.putString("exerciseName", exerciseName);
		
		PerformExerciseFragment fragment = new PerformExerciseFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}
}
