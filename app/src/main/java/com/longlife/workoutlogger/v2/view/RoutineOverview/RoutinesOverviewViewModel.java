package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.Routine;
import com.longlife.workoutlogger.v2.model.comparators.RoutineComparators;
import com.longlife.workoutlogger.v2.utils.Conversions;
import com.longlife.workoutlogger.v2.utils.Response;

import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RoutinesOverviewViewModel
	extends ViewModel
{
	// Static
	private final static String TAG = RoutinesOverviewViewModel.class.getSimpleName();
	// Private
	private final CompositeDisposable disposables = new CompositeDisposable();
	
	// Observable for when inserting a new routine.
	private final Response<Integer> insertResponse = new Response<>();
	// Observable for when requesting list of all routines.
	private final Response<List<Routine>> loadResponse = new Response<>();
	// Observable for when to start creating a new Routine fragment.
	// [TODO] remove
	//private final Response<Boolean> startCreateFragmentResponse = new Response<>();
	// Observable for getting list of exercises.
	private final Response<List<Exercise>> loadExercisesResponse = new Response<>();
	
	private Repository repo;
	private List<Routine> routines;
	private List<Exercise> exercises;
	
	///
	/// Constructors
	///
	public RoutinesOverviewViewModel(Repository repo)
	{
		this.repo = repo;
	}
	
	// Getters
	///
	/// GETTERS
	///
	public List<Routine> getCachedRoutines()
	{
		return routines;
	}
	
	public Observable<Response<Integer>> getInsertResponse()
	{
		return insertResponse.getObservable();
	}
	
	public Observable<Response<List<Exercise>>> getLoadExercisesResponse()
	{
		return loadExercisesResponse.getObservable();
	}
	
	public Observable<Response<List<Routine>>> getLoadResponse()
	{
		return loadResponse.getObservable();
	}
	
	public Single<List<Routine>> getRoutines()
	{
		return (repo.getRoutines());
	}
	
	// [TODO] remove
	/*
	public void startCreateFragment()
	{
		disposables.add(Observable.just(true) // this emitted value does not matter
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(__ -> startCreateFragmentResponse.setLoading())
			.subscribe(b -> startCreateFragmentResponse.setSuccess(b),
				throwable -> startCreateFragmentResponse.setError(throwable)
			)
		);
	}
	*/
	
	public void loadRoutines()
	{
		disposables.add(repo.getRoutines()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(__ -> loadResponse.setLoading())
			.subscribe((List<Routine> ro) -> {
					// sort the list of exercises //[TODO] Set the comparator to what the user chooses
					Collections.sort(ro, RoutineComparators.getDefaultComparator());
					this.routines = ro;
					loadResponse.setSuccess(ro);
				},
				throwable -> loadResponse.setError(throwable)
			)
		);
	}
	
	public void loadExercises()
	{
		disposables.add(
			repo.getExercises()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe(__ -> loadExercisesResponse.setLoading())
				.subscribe((List<Exercise> ex) ->
					{
						this.exercises = ex;
						loadExercisesResponse.setSuccess(ex);
					},
					throwable -> loadExercisesResponse.setError(throwable)
				)
		);
	}
	
	public void insertRoutine(Routine ro)
	{
		disposables.add(
			repo.insertRoutine(ro)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe(__ -> insertResponse.setLoading())
				.subscribe(id ->
					{
						ro.setIdRoutine(Conversions.safeLongToInt(id));
						this.routines.add(ro);
						// sort the list of exercises //[TODO] Set the comparator to what the user chooses
						Collections.sort(this.routines, RoutineComparators.getDefaultComparator());
						for(int i = 0; i < this.routines.size(); i++){
							if(routines.get(i).getIdRoutine() == ro.getIdRoutine()){
								insertResponse.setSuccess(i);
								return;
							}
						}
						Log.d(TAG, "Error: Could not find position of newly inserted exercise.");
						//insertResponse.setSuccess(-1);
					},
					throwable -> insertResponse.setError(throwable)
				)
		);
	}
	
	// Reference: Ala Hammad - https://medium.com/@alahammad/database-with-room-using-rxjava-764ee6124974
	public void deleteRoutine(Routine ro)
	{
		Completable.fromAction(() -> repo.deleteRoutine(ro))
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
	
	/*
	public Observable<Response<Boolean>> startCreateFragmentResponse()
	{
		return startCreateFragmentResponse.getObservable();
	}
	*/
	
	///
	/// UPDATE
	///
	public void updateDisplayOrder(int idRoutine, int order)
	{
		Completable.fromAction(() -> repo.updateDisplayOrder(idRoutine, order))
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
	
	// Overrides
	@Override
	public void onCleared()
	{
		super.onCleared();
		disposables.clear();
	}
}
// Inner Classes
