package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.Profile;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class Repository {

    private final ExerciseDao exerciseDao;
    private final RoutineDao routineDao;
    private final ProfileDao profileDao;

    @Inject
    public Repository(ExerciseDao exerciseDao, RoutineDao routineDao, ProfileDao profileDao) {
        this.exerciseDao = exerciseDao;
        this.routineDao = routineDao;
        this.profileDao = profileDao;
    }

    public Single<List<Exercise>> getExercises() {
        return (exerciseDao.getExercises());
    }

    public Single<List<String>> getExercisesNames() {
        return exerciseDao.getExercisesNames();
    }

    public Maybe<Profile> getProfile() {
        return profileDao.getProfile();
    }

    public Single<List<Routine>> getRoutines() {
        return (routineDao.getRoutines());
    }

    public Single<Exercise> getExercise(String name) {
        return exerciseDao.getExercise(name);
    }

    public Single<List<Exercise>> getExerciseFromId(Set<Long> ids) {
        return exerciseDao.getExerciseFromId(ids);
    }

    public Single<Exercise> getExerciseFromId(Long id) {
        return exerciseDao.getExerciseFromId(id);
    }

    // Update the lock status for an exercise.
    public void updateLockedStatus(Long idExercise, boolean locked) {
        exerciseDao.updateLockedStatus(idExercise, locked);
    }

    // Insert a routine and relevant other table values.
    public Single<Routine> insertRoutineFull(Routine routine, List<RoutineExerciseHelper> reh) {
        return Single.fromCallable(() -> routineDao.insertRoutineFull(routine, reh));
    }

    // Hide/unhide an exercise.
    public void setExerciseHiddenStatus(Long idExercise, boolean hide) {
        exerciseDao.setExerciseHiddenStatus(idExercise, hide ? 1 : 0);
    }

    // Delete routine.
    public void deleteRoutine(Routine ro) {
        routineDao.deleteRoutine(ro);
    }

    // Hide/unhide a routine.
    public void setRoutineAsHidden(Long idRoutine, boolean hide) {
        routineDao.setRoutineAsHidden(idRoutine, hide ? 1 : 0);
    }

    // Update the history for an exercise.
    public Single<Exercise> updateExercise(Exercise ex) {
        return Single.fromCallable(() -> exerciseDao.updateExerciseFull(ex));
    }

    // Insert history for an exercise. This creates a new history value and updates the exercise.
    public Single<Exercise> insertExercise(Exercise exercise) {
        return Single.fromCallable(() -> exerciseDao.insertExerciseFull(exercise));
    }

    // Insert profile.
    public Single<Profile> insertProfile(Profile profileToInsert) {
        return Single.fromCallable(() -> profileDao.insertProfile(profileToInsert))
                .map(idProfile -> {
                    profileToInsert.setIdProfile(idProfile);
                    return profileToInsert;
                });
    }

    // Get latest unperformed exercise session for an exercise.
    public Maybe<SessionExercise> getLatestExerciseSession(Long idExercise) {
        return exerciseDao.getLatestExerciseSession(idExercise);
    }

    public Single<ExerciseSessionWithSets> getSessionExerciseWithSets(Long idSessionExercise) {
        return exerciseDao.getSessionExerciseWithSets(idSessionExercise);
    }

    public Single<SessionExercise> insertNewSessionForExercise(Long idExerciseLeaf) {
        return Single.fromCallable(() -> routineDao.insertNewSessionForExercise(idExerciseLeaf));
    }
}
