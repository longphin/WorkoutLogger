package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.utils.BaseActivity;
import com.longlife.workoutlogger.v2.utils.Response;

public class ExercisesOverviewActivity
	extends BaseActivity
{
	// Static
	private static final String TAG = ExercisesOverviewActivity.class.getSimpleName();
	// Private
	private ExercisesOverviewViewModel viewModel;
	
	// Overrides
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercises_overview);
		
		((MyApplication)getApplication())
			.getApplicationComponent()
			.inject(this);
		
		viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
			ViewModelProviders.of(this, viewModelFactory)
				.get(ExercisesOverviewViewModel.class);
		
		// Add initial fragments.
		initializeFragments();
		
		// Observer for when 'Add new exercise' button is clicked.
		//addDisposable(viewModel.startCreateFragmentResponse().subscribe(response -> processNewExerciseResponse(response)));
	}
	
	// Methods
	private void processNewExerciseResponse(Response<Boolean> response)
	{
		switch(response.getStatus()){
			case LOADING:
				//renderLoadingState();
				break;
			case SUCCESS:
				startCreateExerciseFragment();
				break;
			case ERROR:
				//renderErrorState(response.error);
				break;
		}
	}

	private void startCreateExerciseFragment()
	{
		ExerciseCreateFragment fragment = (ExerciseCreateFragment)manager.findFragmentByTag(ExerciseCreateFragment.TAG);
		if(fragment == null){
			fragment = ExerciseCreateFragment.newInstance();
		}
		
		manager.beginTransaction()
			.replace(R.id.root_exercises_overview, fragment, ExerciseCreateFragment.TAG)
			.addToBackStack(ExerciseCreateFragment.TAG)//(null)
			.commit();
		//addFragmentToActivity(manager, fragment, R.id.root_exercises_overview, ExerciseCreateFragment.TAG, ExerciseCreateFragment.TAG);
		
		Log.d(TAG, "start exercise create fragment");
	}
	
	public void initializeFragments()
	{
		ExercisesFragment fragment = (ExercisesFragment)manager.findFragmentByTag(ExercisesFragment.TAG);
		if(fragment == null){
			fragment = new ExercisesFragment();//ExercisesOverviewFragment.newInstance(R.id.root_exercises_overview, R.layout.item_exercises, R.layout.fragment_exercises_overview);//(R.id.root_exercises_overview);
			/*
			fragment.setRootId(R.id.root_exercises_overview);
			fragment.setItemLayout(R.layout.item_exercises);
			fragment.setOverviewLayout(R.layout.fragment_exercises_overview);
			*/
		}
		
		addFragmentToActivity(manager, fragment, R.id.root_exercises_overview, ExercisesFragment.TAG);
	}
}
// Inner Classes
