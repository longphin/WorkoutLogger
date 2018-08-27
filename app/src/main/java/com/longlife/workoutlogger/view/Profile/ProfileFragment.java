package com.longlife.workoutlogger.view.Profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.R;

// [TODO] Create fragment for the user profile. This will include body weight (saved in a table), preferred measurement system (metric, imperial), name, birthday
public class ProfileFragment
	extends FragmentBase
{
	
	
	public ProfileFragment()
	{
		// Required empty public constructor
	}
	
	// Overrides
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_profile, container, false);
	}
	
	public static Fragment newInstance()
	{
		ProfileFragment fragment = new ProfileFragment();
		
		return fragment;
	}
	
}
