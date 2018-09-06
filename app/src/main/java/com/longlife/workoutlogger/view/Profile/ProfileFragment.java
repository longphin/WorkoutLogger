package com.longlife.workoutlogger.view.Profile;


import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.R;

import javax.inject.Inject;

// [TODO] Create fragment for the user profile. This will include body weight (saved in a table), preferred measurement system (metric, imperial), name, birthday
public class ProfileFragment
	extends FragmentBase
{
	@Inject
	public ViewModelProvider.Factory viewModelFactory;
	
	// Overrides
	
	public ProfileFragment()
	{
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}
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