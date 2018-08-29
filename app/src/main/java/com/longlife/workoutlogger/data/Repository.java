package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseHistory;
import com.longlife.workoutlogger.model.Profile;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class Repository
{
	// Private
	private final ExerciseDao exerciseDao;
	private final RoutineDao routineDao;
	private final ProfileDao profileDao;
	
	@Inject
	public Repository(ExerciseDao exerciseDao, RoutineDao routineDao, ProfileDao profileDao)
	{
		this.exerciseDao = exerciseDao;
		this.routineDao = routineDao;
		this.profileDao = profileDao;
	}
	
	// Getters
	public ExerciseDao getExerciseDao()
	{
		return exerciseDao;
	}
	
	public Maybe<Profile> getProfile()
	{
		return profileDao.getProfile();
	}
	
	public Single<List<Exercise>> getExercises()
	{
		return (exerciseDao.getExercises());
	}
	
	public Single<List<String>> getExercisesNames()
	{
		return exerciseDao.getExercisesNames();
	}
	
	public ProfileDao getProfileDao()
	{
		return profileDao;
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
	public void updateLockedStatus(Long idExercise, boolean locked)
	{
		exerciseDao.updateLockedStatus(idExercise, locked);
	}
	
	///
	/// INSERT methods
	///
	public Single<Routine> insertRoutineFull(Routine routine, List<RoutineExerciseHelper> reh)
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
	
	
	public Single<Exercise> updateExerciseHistoryFull(ExerciseHistory exerciseHistory, Exercise exercise)
	{
		return Single.fromCallable(() -> exerciseDao.updateExerciseHistoryFull(exerciseHistory, exercise));
	}
	
	public Single<Exercise> insertExerciseHistoryFull(Exercise exercise)
	{
		return Single.fromCallable(() -> exerciseDao.insertExerciseHistoryFull(exercise));
	}
	
	public Single<Profile> insertProfile(Profile profileToInsert)
	{
		return Single.fromCallable(() -> profileDao.insertProfile(profileToInsert))
			.map(idProfile -> {
				profileToInsert.setIdProfile(idProfile);
				return profileToInsert;
			});
	}
}
// Inner Classes
