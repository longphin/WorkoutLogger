package com.longlife.workoutlogger.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine.ExercisesSelectableViewModel;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

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
		if(modelClass.isAssignableFrom(RoutinesViewModel.class)){
			return ((T)new RoutinesViewModel(repo));
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