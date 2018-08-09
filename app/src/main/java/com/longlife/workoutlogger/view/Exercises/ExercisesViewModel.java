package com.longlife.workoutlogger.view.Exercises;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.enums.Status;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseHistory;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Exercises.Helper.DeletedExercise;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
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
	
	// Observable for when an exercise is inserted.
	private final Response<Exercise> exerciseEditedResponse = new Response<>();
	
	// Protected
	protected final CompositeDisposable disposables = new CompositeDisposable();
	protected Repository repo;
	
	public ExercisesViewModel(@NonNull Repository repo)
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
	public Observable<Response<Exercise>> getExerciseEditedResponse()
	{
		return exerciseEditedResponse.getObservable();
	}
	
	public Single<Exercise> getExerciseFromId(Long idExercise)
	{
		return repo.getExerciseFromId(idExercise);
	}
	
	public Observable<Response<Exercise>> getExerciseInsertedResponse()
	{
		return exerciseInsertedResponse.getObservable();
	}
	
	public DeletedExercise getFirstDeletedExercise()
	{
		return exercisesToDelete.poll();
	}
	
	public Observable<Response<List<Exercise>>> getLoadResponse()
	{
		Log.d(TAG, "getLoadResponse()");
		return loadResponse.getObservable();
	}
	
	public void addDeletedExercise(Exercise ex, int pos)
	{
		exercisesToDelete.add(new DeletedExercise(ex, pos));
	}
	
	public void loadExercises()
	{
		Log.d(TAG, "loadExercises()");
		if(loadResponse.getStatus() == Status.LOADING)
			return;
		
		disposables.add(repo.getExercises()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(__ -> {
				Log.d(TAG, "loading exercises: loading... ");
				loadResponse.setLoading();
			})
			.subscribe((List<Exercise> ex) -> {
					// sort the list of exercises //[TODO] Set the comparator to what the user chooses
					//Collections.sort(ex, ExerciseComparators.getDefaultComparator());
					//this.exercises = ex;
					Log.d(TAG, "loading exercises: success... ");
					loadResponse.setSuccess(ex);
				},
				throwable ->
				{
					Log.d(TAG, "loading exercises: error... ");
					loadResponse.setError(throwable);
				}
			)
		);
	}
	
	public void insertExercise(Exercise newExercise)
	{
		Log.d(TAG, "insertExercise()");
		if(exerciseInsertedResponse.getStatus() == Status.LOADING)
			return;
		
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
	
	public void updateFavorite(Long idExercise, boolean favorited)
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
	
	public void setExerciseHiddenStatus(Long idExercise, boolean isHidden)
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
	
	public void updateExercise(Exercise exercise)
	{
		Completable.fromAction(() -> repo.updateExercise(exercise))
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new CompletableObserver()
			{
				// Overrides
				@Override
				public void onSubscribe(Disposable d)
				{
				
				}
				
				@Override
				public void onComplete()
				{
				
				}
				
				@Override
				public void onError(Throwable e)
				{
				
				}
			});
	}
	
	public void insertExerciseHistoryFull(ExerciseHistory exerciseHistory, Exercise exercise)
	{
		//return repo.insertExerciseHistory(exerciseHistory);
		//return repo.insertExerciseHistoryFull(exerciseHistory, exercise);
		disposables.add(repo.insertExerciseHistoryFull(exerciseHistory, exercise)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				idExerciseHistory -> {
					exercise.setCurrentIdExerciseHistory(idExerciseHistory);
					exerciseEditedResponse.setSuccess(exercise);
				},
				throwable -> {}
			));
		
/*		disposables.add(repo.insertExerciseHistoryFull(exerciseHistory, exercise)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(__ -> {
				Log.d(TAG, "loading exercises: loading... ");
				exerciseEditedResponse.setLoading();
			})
			.subscribe(idExerciseHistory ->
					// sort the list of exercises //[TODO] Set the comparator to what the user chooses
					//Collections.sort(ex, ExerciseComparators.getDefaultComparator());
					//this.exercises = ex;
					exerciseEditedResponse.setSuccess(idExerciseHistory)
				,
				throwable ->
				{
					Log.d(TAG, "loading exercises: error... ");
					exerciseEditedResponse.setError(throwable);
				}
			)
		);*/
	}
}
