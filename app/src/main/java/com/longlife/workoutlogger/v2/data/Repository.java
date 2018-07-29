package com.longlife.workoutlogger.v2.data;

import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.Routine;
import com.longlife.workoutlogger.v2.view.RoutineOverview.RoutineExerciseHelper;

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
/*	public Long insertRoutine(Routine routine)
	{
		return Flowable.fromCallable(() -> routineDao.insertRoutine(routine))
			.map(idRoutine -> routineDao.insertRoutineSession(idRoutine));
		Long idRoutine = routineDao.insertRoutine(routine);
		RoutineSession routineSession = new RoutineSession(idRoutine);
		return routineDao.insertRoutineSession(routineSession);
	}
	
	private Flowable<Long> insertRoutineSession(RoutineSession rs)
	{
		//return Flowable.fromCallable(() -> routineDao.insertRoutineSession(rs));
		return Flowable.just(1L); // idRoutineSession = 1
	}
	private Flowable<Long> insertSessionExercise(SessionExercise se)
	{
		return Flowable.just(2L);
	}
	private Flowable<List<SessionExercise>> insertSessionExercises(List<SessionExercise> se, Long idRoutineSession)
	{
		return Flowable.fromIterable(se)
			.map(sessionExercise -> {
				sessionExercise.setIdRoutineSession(idRoutineSession);
				return sessionExercise;
			})
			.toList()
			.toFlowable();
	}
	private Flowable<List<RoutineExerciseHelper>> insertRoutineExerciseHelpers(List<RoutineExerciseHelper> reh, Long idRoutineSession)
	{
		return Flowable.fromIterable(reh)
			.map(routineExerciseHelper -> routineExerciseHelper.)
	}
	// return long
	private Flowable<Long> insertRoutineSessionHelpers(ArrayList<RoutineExerciseHelper> reh, List<SessionExercise> se)
	{
		return
			// Insert routine -> idRoutine
			insertRoutine(new Routine())//[TODO]
			// Insert routineSession.setIdRoutine(idRoutine) -> idRoutineSession
			.flatMap((Long idRoutine) -> insertRoutineSession(new RoutineSession(idRoutine)))
			.flatMapIterable((Long idRoutineSession) -> insertRoutineSessionHelpers(reh, idRoutineSession))

	}*/
	public Flowable<Long> insertRoutineFull(Routine routine, List<RoutineExerciseHelper> reh)
	{
		return Flowable.fromCallable(() -> routineDao.insertRoutineFull(routine, reh));
	}
	
	public Flowable<Long> insertExercise(Exercise exercise)
	{
		return Flowable.fromCallable(() -> exerciseDao.insertExercise(exercise));//(Single.fromCallable(() -> exerciseDao.insertExercise(exercise)));
	}
	
	///
	/// DELETE methods
	///
	public void setExerciseAsHidden(int idExercise, boolean isHidden)
	{
		exerciseDao.setExerciseAsHidden(idExercise, isHidden ? 1 : 0);
	}
	
	public void deleteExercise(Exercise ex)
	{
		exerciseDao.deleteExercise(ex);
	}
	
	public void deleteRoutine(Routine ro)
	{
		routineDao.deleteRoutine(ro);
	}
	
	//public void updateExercise(Exercise ex){exerciseDao.updateExercise(ex);}
}
// Inner Classes
