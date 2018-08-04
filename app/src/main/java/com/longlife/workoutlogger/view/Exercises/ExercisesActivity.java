package com.longlife.workoutlogger.view.Exercises;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;

import com.longlife.workoutlogger.AndroidUtils.ActivityBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;

public class ExercisesActivity
	extends ActivityBase
{
	// Static
	private static final String TAG = ExercisesActivity.class.getSimpleName();
	// Private
	private ExercisesViewModel viewModel;
	
	// Overrides
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercises);
		
		((MyApplication)getApplication())
			.getApplicationComponent()
			.inject(this);
		
		viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
			ViewModelProviders.of(this, viewModelFactory)
				.get(ExercisesViewModel.class);
		
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
			.replace(R.id.root_exercises, fragment, ExerciseCreateFragment.TAG)
			.addToBackStack(ExerciseCreateFragment.TAG)//(null)
			.commit();
		//addFragmentToActivity(manager, fragment, R.id.root_exercises_overview, ExerciseCreateFragment.TAG, ExerciseCreateFragment.TAG);
		
		Log.d(TAG, "start exercise create fragment");
	}
	
	public void initializeFragments()
	{
		ExercisesFragment fragment = (ExercisesFragment)manager.findFragmentByTag(ExercisesFragment.TAG);
		if(fragment == null){
			fragment = new ExercisesFragment();//ExercisesOverviewFragment.newInstance(R.id.root_exercises_overview, R.layout.item_exercises, R.layout.fragment_exercises);//(R.id.root_exercises_overview);
			fragment.setAdapter(new ExercisesAdapter(viewModel));
			fragment.setRootId(R.id.root_exercises);
			fragment.setLayoutId(R.layout.fragment_exercises);
			/*
			fragment.setItemLayout(R.layout.item_exercises);
			fragment.setOverviewLayout(R.layout.fragment_exercises);
			*/
		}
		
		addFragmentToActivity(manager, fragment, R.id.root_exercises, ExercisesFragment.TAG);
	}
}
// Inner Classes
