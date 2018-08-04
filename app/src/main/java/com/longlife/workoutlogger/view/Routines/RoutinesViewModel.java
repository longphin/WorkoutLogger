package com.longlife.workoutlogger.view.Routines;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.comparators.RoutineComparators;
import com.longlife.workoutlogger.utils.Conversions;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Routines.Helper.DeletedRoutine;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.Collections;
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

public class RoutinesViewModel
	extends ViewModel
{
	// Static
	private final static String TAG = RoutinesViewModel.class.getSimpleName();
	// Private
	private final CompositeDisposable disposables = new CompositeDisposable();
	
	// Observable for when inserting a new routine.
	private final Response<Routine> insertResponse = new Response<>();
	// Observable for when requesting list of all routines.
	private final Response<List<Routine>> loadResponse = new Response<>();
	// Observable for getting list of exercises.
	private final Response<List<Exercise>> loadExercisesResponse = new Response<>();
	
	private Repository repo;
	
	///
	/// Constructors
	///
	public RoutinesViewModel(Repository repo)
	{
		this.repo = repo;
	}
	
	///
	/// UPDATE
	///
	private Queue<DeletedRoutine> routinesToDelete = new LinkedList<>();
	///
	/// GETTERS
	///
	@Override
	public void onCleared()
	{
		super.onCleared();
		disposables.clear();
	}
	
	// Overrides
	// Getters
	
	public Observable<Response<List<Exercise>>> getLoadExercisesResponse()
	{
		return loadExercisesResponse.getObservable();
	}
	
	public Observable<Response<List<Routine>>> getLoadResponse()
	{
		return loadResponse.getObservable();
	}
	
	public DeletedRoutine getFirstDeletedRoutine()
	{
		return routinesToDelete.poll();
	}
	
	public Observable<Response<Routine>> getInsertResponse()
	{
		return insertResponse.getObservable();
	}
	
	public void loadRoutines()
	{
		disposables.add(repo.getRoutines()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(__ -> loadResponse.setLoading())
			.subscribe((List<Routine> ro) -> {
					// sort the list of exercises //[TODO] Set the comparator to what the user chooses
					Collections.sort(ro, RoutineComparators.getDefaultComparator());
					//this.routines = ro;
					loadResponse.setSuccess(ro);
				},
				throwable -> loadResponse.setError(throwable)
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
	
	public void loadExercises()
	{
		disposables.add(
			repo.getExercises()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe(__ -> loadExercisesResponse.setLoading())
				.subscribe((List<Exercise> ex) ->
					{
						//this.exercises = ex;
						loadExercisesResponse.setSuccess(ex);
					},
					throwable -> loadExercisesResponse.setError(throwable)
				)
		);
	}

	public void insertRoutineFull(Routine ro, List<RoutineExerciseHelper> reh)
	{
		disposables.add(
			repo.insertRoutineFull(ro, reh)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe(__ -> insertResponse.setLoading())
				.subscribe(idRoutine ->
					{
						ro.setIdRoutine(Conversions.safeLongToInt(idRoutine));
						//this.routines.add(ro);
						// sort the list of exercises //[TODO] Set the comparator to what the user chooses
						
						/*Collections.sort(this.routines, RoutineComparators.getDefaultComparator());
						for(int i = 0; i < this.routines.size(); i++){
							if(routines.get(i).getIdRoutine() == id){
								insertResponse.setSuccess(i);
								return;
							}
						}*/
						//Log.d(TAG, "Error: Could not find position of newly inserted exercise.");
						//insertResponse.setSuccess(-1);
						insertResponse.setSuccess(ro);
					},
					throwable -> insertResponse.setError(throwable)
				)
		);
	}
	
	public void addDeletedRoutine(Routine deletedItem, int position)
	{
		routinesToDelete.add(new DeletedRoutine(deletedItem, position));
	}
	
	public void setRoutineHiddenStatus(int idRoutine, boolean isHidden)
	{
		Completable.fromAction(() -> repo.setRoutineAsHidden(idRoutine, isHidden))
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
								 }
			);
	}
}
// Inner Classes
