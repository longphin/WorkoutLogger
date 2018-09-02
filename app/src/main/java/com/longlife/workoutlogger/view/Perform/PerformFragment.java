package com.longlife.workoutlogger.view.Perform;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.view.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerformFragment
	extends FragmentBase
{
	public static final String TAG = PerformFragment.class.getSimpleName();
	
	public PerformFragment()
	{
		// Required empty public constructor
	}
	
	
	// Overrides
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		
		((MainActivity)getActivity()).updateToolbarTitle(getString(R.string.Toolbar_PerformExercise));
		
		return inflater.inflate(R.layout.fragment_perform, container, false);
	}
	
	public static PerformFragment newInstance()
	{
		PerformFragment fragment = new PerformFragment();
		
		return fragment;
	}
}
