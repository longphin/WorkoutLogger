package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.view.Routines.Helpers.RoutineExerciseHelper;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

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
	
	public Single<List<Routine>> getRoutines()
	{
		return (routineDao.getRoutines());
	}
	
	public Single<Routine> getRoutine(int idRoutine)
	{
		return (routineDao.getRoutine(idRoutine));
	}
	
	public Single<Exercise> getExercise(String name)
	{
		return exerciseDao.getExercise(name);
	}
	
	public Single<List<Exercise>> getExerciseFromId(Set<Integer> ids){ return exerciseDao.getExerciseFromId(ids);}
	
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
	public Single<Long> insertRoutineFull(Routine routine, List<RoutineExerciseHelper> reh)
	{
		return Single.fromCallable(() -> routineDao.insertRoutineFull(routine, reh));
	}
	
	public Single<Long> insertExercise(Exercise exercise)
	{
		return Single.fromCallable(() -> exerciseDao.insertExercise(exercise));//(Single.fromCallable(() -> exerciseDao.insertExercise(exercise)));
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
