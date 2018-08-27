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
import com.longlife.workoutlogger.view.Exercises.Helper.ExerciseFavorited;

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
import io.reactivex.subjects.PublishSubject;

public class ExercisesViewModel
	extends ViewModel
{
	// Static
	private final static String TAG = ExercisesViewModel.class.getSimpleName();
	// Private
	private final Response<Exercise> exerciseInsertedResponse = new Response<>();
	
	// Observable for when requesting list of all exercises.
	private final Response<List<Exercise>> loadExercisesResponse = new Response<>();
	// Observable for when an exercise was favorited.
	private final PublishSubject<ExerciseFavorited> exerciseFavoritedObservable = PublishSubject.create();
	// Observable for when an exercise is inserted.
	//private final Response<Exercise> exerciseEditedResponse = new Response<>();
	private final PublishSubject<Exercise> exerciseEditedObservable = PublishSubject.create();
	private final CompositeDisposable disposables = new CompositeDisposable();
	private Queue<DeletedExercise> exercisesToDelete = new LinkedList<>();
	private Repository repo;
	
	// Protected
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
	/*	public Observable<Response<Exercise>> getExerciseEditedResponse()
		{
			return exerciseEditedResponse.getObservable();
		}*/
	public PublishSubject<Exercise> getExerciseEditedObservable()
	{
		return exerciseEditedObservable;
	}
	
	public PublishSubject<ExerciseFavorited> getExerciseFavoritedObservable()
	{
		return exerciseFavoritedObservable;
	}
	
	public Observable<Response<Exercise>> getExerciseInsertedResponse()
	{
		return exerciseInsertedResponse.getObservable();
	}
	
	public DeletedExercise getFirstDeletedExercise()
	{
		return exercisesToDelete.poll();
	}
	
	public Observable<Response<List<Exercise>>> getLoadExercisesResponse()
	{
		return loadExercisesResponse.getObservable();
	}
	
	public Single<Exercise> getExerciseFromId(Long idExercise)
	{
		return repo.getExerciseFromId(idExercise);
	}
	
	public void addDeletedExercise(Exercise ex, int pos)
	{
		exercisesToDelete.add(new DeletedExercise(ex, pos));
	}
	
	// Get observable for all exercise names.
	public Single<List<String>> loadExerciseNames()
	{
		return repo.getExercisesNames()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());
	}
	
	public void loadExercises()
	{
		Log.d(TAG, "loadExercises()");
		if(loadExercisesResponse.getStatus() == Status.LOADING)
			return;
		
		disposables.add(repo.getExercises()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(__ -> {
				Log.d(TAG, "loading exercises: loading... ");
				loadExercisesResponse.setLoading();
			})
			.subscribe((List<Exercise> ex) -> {
					// sort the list of exercises //[TODO] Set the comparator to what the user chooses
					//Collections.sort(ex, ExerciseComparators.getDefaultComparator());
					//this.exercises = ex;
					Log.d(TAG, "loading exercises: success... ");
					loadExercisesResponse.setSuccess(ex);
				},
				throwable ->
				{
					Log.d(TAG, "loading exercises: error... ");
					loadExercisesResponse.setError(throwable);
				}
			)
		);
	}
	
	public void insertExercise(Exercise newExercise)
	{
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
					exerciseFavoritedObservable.onNext(new ExerciseFavorited(idExercise, favorited));
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
	
	public void updateExerciseHistoryFull(ExerciseHistory exerciseHistory, Exercise exercise)
	{
		disposables.add(repo.updateExerciseHistoryFull(exerciseHistory, exercise)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				(Exercise updatedExercise) -> {
					//exerciseEditedResponse.setSuccess(exercise);
					exerciseEditedObservable.onNext(updatedExercise);
				},
				throwable -> {}
			));
	}
	
	public void insertExerciseHistoryFull(Exercise exercise)
	{
		disposables.add(repo.insertExerciseHistoryFull(exercise)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				insertedExercise -> {
					exerciseInsertedResponse.setSuccess(insertedExercise);
				},
				throwable -> {}
			));
	}
}
