package com.longlife.workoutlogger.v2.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesOverviewViewModel;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesSelectableViewModel;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesViewModel;
import com.longlife.workoutlogger.v2.view.RoutineOverview.RoutinesOverviewViewModel;

import javax.inject.Inject;

public class CustomViewModelFactory
	implements ViewModelProvider.Factory
{
	// Private
	private final Repository repo;
	
	@Inject
	public CustomViewModelFactory(Repository repo)
	{
		this.repo = repo;
	}
	
	// Overrides
	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
	{
		if(modelClass.isAssignableFrom(RoutinesOverviewViewModel.class)){
			return ((T)new RoutinesOverviewViewModel(repo));
		}else if(modelClass.isAssignableFrom(ExercisesOverviewViewModel.class)){
			return ((T)new ExercisesOverviewViewModel(repo));
		}else if(modelClass.isAssignableFrom(ExercisesViewModel.class)){
			return ((T)new ExercisesViewModel(repo));
		}else if(modelClass.isAssignableFrom(ExercisesSelectableViewModel.class)){
			return ((T)new ExercisesSelectableViewModel(repo));
		}else{
			throw new IllegalArgumentException("ViewModel not found");
		}
		
	}
}
// Inner Classes
