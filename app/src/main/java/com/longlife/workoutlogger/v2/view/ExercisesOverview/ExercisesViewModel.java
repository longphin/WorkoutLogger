package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.Response;
import com.longlife.workoutlogger.v2.utils.Status;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ExercisesViewModel
	extends ViewModel
{
	// Static
	private final static String TAG = ExercisesOverviewViewModel.class.getSimpleName();
	// Private
	private final CompositeDisposable disposables = new CompositeDisposable();
	
	// Observable for when requesting list of all exercises.
	private final Response<List<Exercise>> loadResponse = new Response<>();
	private Repository repo;
	
	public ExercisesViewModel(Repository repo)
	{
		this.repo = repo;
	}
	
	// Overrides
	@Override
	public void onCleared()
	{
		super.onCleared();
		disposables.clear();
	}
	
	// Getters
	public Observable<Response<List<Exercise>>> getLoadResponse()
	{
		Log.d(TAG, "getLoadResponse()");
		return loadResponse.getObservable();
	}
	
	public void loadExercises()
	{
		Log.d(TAG, "loadExercises()");
		if(loadResponse.getStatus() == Status.LOADING)
			return;
		Log.d(TAG, "loadExercises() continued");
		disposables.add(repo.getExercises()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(__ -> loadResponse.setLoading())
			.subscribe((List<Exercise> ex) -> {
					// sort the list of exercises //[TODO] Set the comparator to what the user chooses
					//Collections.sort(ex, ExerciseComparators.getDefaultComparator());
					//this.exercises = ex;
					loadResponse.setSuccess(ex);
				},
				throwable -> loadResponse.setError(throwable)
			)
		);
	}
}
