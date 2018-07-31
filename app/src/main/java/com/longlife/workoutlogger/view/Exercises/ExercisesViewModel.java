package com.longlife.workoutlogger.view.Exercises;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.enums.Status;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Exercises.Helper.DeletedExercise;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ExercisesViewModel
	extends ViewModel
{
	// Static
	private final static String TAG = ExercisesViewModel.class.getSimpleName();
	// Private
	private final Response<Exercise> exerciseInsertedResponse = new Response<>();
	
	// Observable for when requesting list of all exercises.
	private final Response<List<Exercise>> loadResponse = new Response<>();
	private Queue<DeletedExercise> exercisesToDelete = new LinkedList<>();
	protected final CompositeDisposable disposables = new CompositeDisposable();
	
	// Protected
	
	protected Repository repo;
	
	public ExercisesViewModel(@NonNull Repository repo)
	{
		this.repo = repo;
	}
	// Overrides
	// Getters
	public DeletedExercise getFirstDeletedExercise()
	{
		return exercisesToDelete.poll();
	}
	@Override
	public void onCleared()
	{
		super.onCleared();
		disposables.clear();
	}
	
	public void addDeletedExercise(Exercise ex, int pos)
	{
		exercisesToDelete.add(new DeletedExercise(ex, pos));
	}
	public Observable<Response<List<Exercise>>> getLoadResponse()
	{
		Log.d(TAG, "getLoadResponse()");
		return loadResponse.getObservable();
	}
	
	public Observable<Response<Exercise>> getExerciseInsertedResponse()
	{
		return exerciseInsertedResponse.getObservable();
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
	
	public void insertExercise(Exercise newExercise)
	{
		Log.d(TAG, "insertExercise()");
		if(exerciseInsertedResponse.getStatus() == Status.LOADING)
			return;
		
		Log.d(TAG, "insertExercise() continued");
		disposables.add(repo.insertExercise(newExercise)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(__ -> exerciseInsertedResponse.setLoading())
			.subscribe(idExercise ->
				{
					newExercise.setIdExercise(idExercise);
					exerciseInsertedResponse.setSuccess(newExercise);
				},
				throwable -> exerciseInsertedResponse.setError(throwable)
			)
		);
	}
	
	public void updateFavorite(int idExercise, boolean favorited)
	{
		Log.d(TAG, "updateFavorite");
		Completable.fromAction(() -> repo.updateFavorite(idExercise, favorited))
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new CompletableObserver()
			{
				// Overrides
				@Override
				public void onSubscribe(Disposable d){}
				
				@Override
				public void onComplete()
				{
					Log.d(TAG, "Update successful.");
				}
				
				@Override
				public void onError(Throwable e)
				{
					e.getMessage();
				}
			});
	}
	
	public void setExerciseHiddenStatus(int idExercise, boolean isHidden)
	{
		Completable.fromAction(() -> repo.setExerciseAsHidden(idExercise, isHidden))
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new CompletableObserver()
			{
				// Overrides
				@Override
				public void onSubscribe(Disposable d){}
				
				@Override
				public void onComplete()
				{
				}
				
				@Override
				public void onError(Throwable e)
				{
					e.getMessage();
				}
			});
	}
}
