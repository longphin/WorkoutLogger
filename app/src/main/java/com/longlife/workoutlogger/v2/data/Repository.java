package com.longlife.workoutlogger.v2.data;

import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class Repository
{
	// Private
	private final ExerciseDao exerciseDao;
	private final RoutineDao routineDao;
	
	@Inject
	public Repository(ExerciseDao exerciseDao, RoutineDao routineDao)
	{
		this.exerciseDao = exerciseDao;
		this.routineDao = routineDao;
	}
	
	// Getters
	public ExerciseDao getExerciseDao()
	{
		return exerciseDao;
	}
	
	public Single<List<Exercise>> getExercises()
	{
		return (exerciseDao.getExercises());
	}
	
	public RoutineDao getRoutineDao()
	{
		return routineDao;
	}
	
	///
	/// GET methods
	///
	public Single<List<Routine>> getRoutines()
	{
		return (routineDao.getRoutines());
	}
	
	public Single<Routine> getRoutine(int idRoutine)
	{
		return (routineDao.getRoutine(idRoutine));
	}
	
	public Maybe<Exercise> getExercise(String name)
	{
		return exerciseDao.getExercise(name);
	}
	
	// UPDATES
	public void updateFavorite(int idExercise, boolean favorited)
	{
		exerciseDao.updateFavorite(idExercise, favorited);
	}
	
	public void updateDisplayOrder(int idRoutine, int order)
	{
		routineDao.updateDisplayOrder(idRoutine, order);
	}
	
	///
	/// INSERT methods
	///
	public Flowable<Long> insertRoutine(Routine routine)
	{
		return Flowable.fromCallable(() -> routineDao.insertRoutine(routine));
	}
	
	public Flowable<Long> insertExercise(Exercise exercise)
	{
		return Flowable.fromCallable(() -> exerciseDao.insertExercise(exercise));//(Single.fromCallable(() -> exerciseDao.insertExercise(exercise)));
	}
	
	///
	/// DELETE methods
	///
	public void deleteExercise(Exercise ex)
	{
		exerciseDao.deleteExercise(ex);
	}
	
	public void deleteRoutine(Routine ro)
	{
		routineDao.deleteRoutine(ro);
	}
}
// Inner Classes
