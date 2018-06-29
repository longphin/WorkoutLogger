package com.longlife.workoutlogger.v2.data;

import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.Routine;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Flowable;

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
	
	public Flowable<List<Exercise>> getExercises()
	{
		return (exerciseDao.getExercises());
	}
	
	public RoutineDao getRoutineDao()
	{
		return routineDao;
	}
	
	public Flowable<List<Routine>> getRoutines()
	{
		return (routineDao.getRoutines());
	}
	
	public Flowable<Routine> getRoutine(int idRoutine)
	{
		return (routineDao.getRoutine(idRoutine));
	}
	
	public Flowable<Exercise> getExercise(String name)
	{
		return exerciseDao.getExercise(name);
	}
	
	//public Flowable<List<Exercise>> getExerciseFromId(List<Integer> ids){ return exerciseDao.getExerciseFromId(ids);}
	public Flowable<List<Exercise>> getExerciseFromId(Set<Integer> ids){ return exerciseDao.getExerciseFromId(ids);}
	
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
