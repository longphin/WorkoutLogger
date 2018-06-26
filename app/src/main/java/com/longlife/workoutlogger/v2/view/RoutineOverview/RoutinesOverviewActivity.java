package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.utils.BaseActivity;
import com.longlife.workoutlogger.v2.utils.Response;

public class RoutinesOverviewActivity
	extends BaseActivity
{
	// Static
	private static final String TAG = RoutinesOverviewActivity.class.getSimpleName();
	
	private RoutinesOverviewViewModel viewModel;
	
	// Methods
	private void processNewRoutineResponse(Response<Boolean> response)
	{
		switch(response.getStatus()){
			case LOADING:
				//renderLoadingState();
				break;
			case SUCCESS:
				startCreateRoutineFragment();
				break;
			case ERROR:
				//renderErrorState(response.error);
				break;
		}
	}
	
	private void startCreateRoutineFragment()
	{
		RoutineCreateFragment fragment = (RoutineCreateFragment)manager.findFragmentByTag(RoutineCreateFragment.TAG);
		if(fragment == null){
			fragment = RoutineCreateFragment.newInstance();

            /*
            manager.beginTransaction()
                    .replace(R.id.root_routines_overview, fragment, RoutineCreateFragment.TAG)
                    .addToBackStack(null)
                    .commit();
            */
		}
		
		addFragmentToActivity(manager, fragment, R.id.root_routines_overview, RoutineCreateFragment.TAG, RoutineCreateFragment.TAG);
		
		Log.d(TAG, "start routine create fragment");
	}
	
	public void initializeFragments()
	{
		RoutinesOverviewFragment fragment = (RoutinesOverviewFragment)manager.findFragmentByTag(RoutinesOverviewFragment.TAG);
		if(fragment == null){
			fragment = RoutinesOverviewFragment.newInstance();
		}
		
		addFragmentToActivity(manager, fragment, R.id.root_routines_overview, RoutinesOverviewFragment.TAG);
	}
	
	// Overrides
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routines_overview);
		
		((MyApplication)getApplication())
			.getApplicationComponent()
			.inject(this);
		
		viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
			ViewModelProviders.of(this, viewModelFactory)
				.get(RoutinesOverviewViewModel.class);
		
		// Initialize RoutinesOverviewFragment.
		initializeFragments();
		
		// Initialize a subscriber that observes when to start a RoutineCreateFragment.
		addDisposable(viewModel.startCreateFragmentResponse().subscribe(response -> processNewRoutineResponse(response)));
	}
}
// Inner Classes
