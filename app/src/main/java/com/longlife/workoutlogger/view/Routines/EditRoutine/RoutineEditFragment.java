package com.longlife.workoutlogger.view.Routines.EditRoutine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.view.MainActivity;

// [TODO] Need to implement for when editing a routine.
public class RoutineEditFragment
	extends FragmentBase
{
	public static final String TAG = RoutineEditFragment.class.getSimpleName();
	
	private Long idRoutine;
	private View mView;
	
	public RoutineEditFragment()
	{
		// Required empty public constructor
	}
	
	// Overrides
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		idRoutine = getArguments().getLong("idRoutine");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		if(mView == null){
			mView = inflater.inflate(R.layout.fragment_routine_edit, container, false);
		}
		
		((MainActivity)getActivity()).updateToolbarTitle(getString(R.string.Toolbar_RoutineEdit));
		return mView;
	}
	
	public static RoutineEditFragment newInstance(Long idRoutine)
	{
		Bundle bundle = new Bundle();
		bundle.putLong("idRoutine", idRoutine);
		
		RoutineEditFragment fragment = new RoutineEditFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
}
