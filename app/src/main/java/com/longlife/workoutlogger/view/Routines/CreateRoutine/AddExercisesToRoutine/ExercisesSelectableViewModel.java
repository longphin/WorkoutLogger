package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine;

import android.util.Log;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.enums.Status;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExercisesSelectableViewModel
	extends ExercisesViewModel
{
	// Static
	private final static String TAG = ExercisesSelectableViewModel.class.getSimpleName();
	// Private
	private final Response<List<Exercise>> addExercisesToRoutine = new Response<>();
	private Set<Integer> selectedIdExercises = new HashSet<>();
	
	public ExercisesSelectableViewModel(Repository repo)
	{
		super(repo);
	}
	
	// Getters
	public Observable<Response<List<Exercise>>> getAddExercisesToRoutineResponse()
	{
		return addExercisesToRoutine.getObservable();
	}
	
	public void clearIdSelectedExercises(){selectedIdExercises.clear();}
	
	public boolean isIdSelected(int idExercise)
	{
		return selectedIdExercises.contains(idExercise);
	}
	
	public void removeSelectedExercise(int idExercise)
	{
		selectedIdExercises.remove(idExercise);
	}
	
	public void addSelectedExercise(int idExercise)
	{
		selectedIdExercises.add(idExercise);
	}
	
	public void addExercisesToRoutine()
	{
		Log.d(TAG, "addExercisesToRoutine()");
		if(addExercisesToRoutine.getStatus() == Status.LOADING)
			return;
		Log.d(TAG, "addExercisesToRoutine() continued");
		disposables.add(repo.getExerciseFromId(selectedIdExercises)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(__ -> addExercisesToRoutine.setLoading())
			.subscribe((List<Exercise> exercises) ->
				{
					addExercisesToRoutine.setSuccess(exercises);
				},
				throwable -> addExercisesToRoutine.setError(throwable)
			)
		);
	}
}
