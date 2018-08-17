package com.longlife.workoutlogger.view.Routines;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.longlife.workoutlogger.AndroidUtils.ActivityBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;

public class RoutinesActivity
	extends ActivityBase
{
	// Static
	private static final String TAG = RoutinesActivity.class.getSimpleName();
	
	private RoutinesViewModel viewModel;
	
	// Overrides
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routines);
		
		((MyApplication)getApplication())
			.getApplicationComponent()
			.inject(this);
		
		viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
			ViewModelProviders.of(this, viewModelFactory)
				.get(RoutinesViewModel.class);
		
		// Initialize RoutinesFragment.
		initializeFragments();
		
		// Initialize a subscriber that observes when to start a RoutineCreateFragment.
		//addDisposable(viewModel.startCreateFragmentResponse().subscribe(response -> processNewRoutineResponse(response)));
	}
	
	// Methods
	public void initializeFragments()
	{
		RoutinesFragment fragment = (RoutinesFragment)manager.findFragmentByTag(RoutinesFragment.TAG);
		if(fragment == null){
			fragment = RoutinesFragment.newInstance();
		}
		
		addFragmentToActivity(manager, fragment, R.id.root_routines, RoutinesFragment.TAG);
	}
}
// Inner Classes
