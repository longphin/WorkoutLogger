package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseHistory;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

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
	
	public Single<List<String>> getExercisesNames()
	{
		return exerciseDao.getExercisesNames();
	}
	
	public RoutineDao getRoutineDao()
	{
		return routineDao;
	}
	
	public Single<List<Routine>> getRoutines()
	{
		return (routineDao.getRoutines());
	}
	
	public Single<Routine> getRoutine(Long idRoutine)
	{
		return (routineDao.getRoutine(idRoutine));
	}
	
	public Single<Exercise> getExercise(String name)
	{
		return exerciseDao.getExercise(name);
	}
	
	public Single<List<Exercise>> getExerciseFromId(Set<Long> ids){ return exerciseDao.getExerciseFromId(ids);}
	
	public Single<Exercise> getExerciseFromId(Long id){return exerciseDao.getExerciseFromId(id);}
	
	// UPDATES
	public void updateFavorite(Long idExercise, boolean favorited)
	{
		exerciseDao.updateFavorite(idExercise, favorited);
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
			// Insert routineSession.setIdRoutineHistory(idRoutine) -> idRoutineSession
			.flatMap((Long idRoutine) -> insertRoutineSession(new RoutineSession(idRoutine)))
			.flatMapIterable((Long idRoutineSession) -> insertRoutineSessionHelpers(reh, idRoutineSession))

	}*/
	public Single<Long> insertRoutineFull(Routine routine, List<RoutineExerciseHelper> reh)
	{
		return Single.fromCallable(() -> routineDao.insertRoutineFull(routine, reh));
	}
	
	public Single<Long> insertExercise(Exercise exercise)
	{
		return Single.fromCallable(() -> exerciseDao.insertExerciseFull(exercise));
	}
	
	///
	/// DELETE methods
	///
	public void setExerciseAsHidden(Long idExercise, boolean isHidden)
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
	
	public void setRoutineAsHidden(Long idRoutine, boolean b)
	{
		routineDao.setRoutineAsHidden(idRoutine, b ? 1 : 0);
	}
	
	public void updateExercise(Exercise exercise)
	{
		exerciseDao.updateExercise(exercise);
	}
	
	public Single<Exercise> updateExerciseHistoryFull(ExerciseHistory exerciseHistory, Exercise exercise)
	{
		return Single.fromCallable(() -> exerciseDao.updateExerciseHistoryFull(exerciseHistory, exercise));
	}
	
	public Single<Exercise> insertExerciseHistoryFull(Exercise exercise)
	{
		return Single.fromCallable(() -> exerciseDao.insertExerciseHistoryFull(exercise));
	}
}
// Inner Classes
