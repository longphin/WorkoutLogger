package com.longlife.workoutlogger.v2.view.ExercisesOverview;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.Conversions;
import com.longlife.workoutlogger.v2.utils.Response;
import com.longlife.workoutlogger.v2.utils.Status;

import java.util.List;
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ExercisesOverviewViewModel
	extends ViewModel
{
	// Static
	private final static String TAG = ExercisesOverviewViewModel.class.getSimpleName();
	// Private
	private final CompositeDisposable disposables = new CompositeDisposable();
	
	// Observable for when inserting a new exercise.
	private final Response<ExerciseInsertHelper> insertResponse = new Response<>();
	// Observable for when requesting list of all exercises.
	private final Response<List<Exercise>> loadResponse = new Response<>();
	// Observable for when adding exercises to a routine.
	private final Response<List<Exercise>> addExercisesToRoutine = new Response<>();
	
	private Repository repo;
	//private List<Exercise> exercises;
	
	//private Set<Integer> selectedIdExercises = new HashSet<>();
	
	///
	/// Constructors
	///
	public ExercisesOverviewViewModel(Repository repo)
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
	
	public Observable<Response<ExerciseInsertHelper>> getInsertResponse()
	{
		Log.d(TAG, "getInsertResponse");
		return insertResponse.getObservable();
	}
	
	/*
	public void addExercisesToRoutine(List<Integer> ids)
	{
		if(addExercisesToRoutine.getStatus() == Status.LOADING) return;
		
		disposables.add(repo.getExerciseFromId(ids)
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
	*/
	
	public Observable<Response<List<Exercise>>> getLoadResponse()
	{
		Log.d(TAG, "getLoadResponse()");
		return loadResponse.getObservable();
	}
	
	public Observable<Response<List<Exercise>>> getSelectedExercises()
	{
		Log.d(TAG, "getSelectedExercises()");
		return addExercisesToRoutine.getObservable();
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
	
	public void insertExercise(Exercise ex)
	{
		Log.d(TAG, "insertExercise()");
		if(insertResponse.getStatus() == Status.LOADING)
			return;
		Log.d(TAG, "insertExercise() continued");
		disposables.add(
			repo.insertExercise(ex)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe(__ -> insertResponse.setLoading())
				.subscribe(id ->
					{
						ex.setIdExercise(Conversions.safeLongToInt(id));
						//this.exercises.add(ex);
						// sort the list of exercises //[TODO] Set the comparator to what the user chooses
						/*
						Collections.sort(this.exercises, ExerciseComparators.getDefaultComparator());
						for(int i = 0; i < this.exercises.size(); i++){
							if(exercises.get(i).getIdExercise() == ex.getIdExercise()){
								insertResponse.setSuccess(new ExerciseInsertHelper(ex, i));
								return;
							}
						}
						insertResponse.setError(new NoSuchElementException("Could not find position of inserted exercise."));
						*/
						insertResponse.setSuccess(new ExerciseInsertHelper(ex, 0));
					},
					throwable -> insertResponse.setError(throwable)
				)
		);
	}
	
	// Reference: Ala Hammad - https://medium.com/@alahammad/database-with-room-using-rxjava-764ee6124974
	public void deleteExercise(Exercise ex)
	{
		Log.d(TAG, "deleteExercise()");
		Completable.fromAction(() -> repo.deleteExercise(ex))
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
					Log.d(TAG, "Delete successful.");
				}
				
				@Override
				public void onError(Throwable e)
				{
					e.getMessage();
				}
			});
	}
	
	///
	/// UPDATE
	///
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
				public void onSubscribe(Disposable d)
				{
				}
				
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
	
	public void addExercisesToRoutine(Set<Integer> ids)
	{
		Log.d(TAG, "addExercisesToRoutine()");
		if(addExercisesToRoutine.getStatus() == Status.LOADING)
			return;
		Log.d(TAG, "addExercisesToRoutine() continued");
		disposables.add(repo.getExerciseFromId(ids)
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
// Inner Classes
