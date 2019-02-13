/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 7:36 PM.
 */

package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.Exercise.ExerciseWithMuscleGroup;
import com.longlife.workoutlogger.model.ExerciseMuscle;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.MuscleEntity;
import com.longlife.workoutlogger.model.MuscleGroupEntity;
import com.longlife.workoutlogger.model.Profile;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.WorkoutProgram;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class Repository {

    private final ExerciseDao exerciseDao;
    private final RoutineDao routineDao;
    private final ProfileDao profileDao;
    private final WorkoutDao workoutDao;

    @Inject
    public Repository(ExerciseDao exerciseDao, RoutineDao routineDao, ProfileDao profileDao, WorkoutDao workoutDao) {
        this.exerciseDao = exerciseDao;
        this.routineDao = routineDao;
        this.profileDao = profileDao;
        this.workoutDao = workoutDao;
    }

    public Single<List<Exercise>> getExercises() {
        return (exerciseDao.getExercises());
    }

    public Single<List<ExerciseWithMuscleGroup>> getExercisesByMuscleGroup(int idMuscleGroup) {
        return exerciseDao.getExerciseByMuscleGroup(idMuscleGroup);
    }

    public Single<List<ExerciseShort>> getExerciseShort() {
        return exerciseDao.getExerciseShort();
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
    public Single<ExerciseShort> setExerciseHiddenStatus(ExerciseShort exercise, boolean hide) {
        return Single.fromCallable(() ->
        {
            exerciseDao.setExerciseHiddenStatus(exercise.getIdExercise(), hide ? 1 : 0);
            return exercise;
        });
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
    public void updateExercise(ExerciseUpdated ex, Set<ExerciseMuscle> relatedMuscles, Set<ExerciseMuscle> musclesToDelete) {
        exerciseDao.updateExercise(ex, relatedMuscles, musclesToDelete);//ex.getIdExercise(), ex.getName(), ex.getNote());
    }

    // Insert an exercise.
    public Single<Exercise> insertExercise(Exercise exercise, Set<ExerciseMuscle> muscles) {
        return Single.fromCallable(() -> exerciseDao.insertExerciseFull(exercise, muscles));
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

    // Get latest unsaved workout program.
    public Maybe<WorkoutProgram> getFirstUnsavedWorkout() {
        return workoutDao.getFirstNonSavedWorkout();
    }

    public Single<ExerciseSessionWithSets> getSessionExerciseWithSets(Long idSessionExercise) {
        return exerciseDao.getSessionExerciseWithSets(idSessionExercise);
    }

    public Single<SessionExercise> insertNewSessionForExercise(Long idExercise, String note) {
        return Single.fromCallable(() -> routineDao.insertNewSessionForExercise(idExercise, note));
    }

    public Single<ExerciseUpdated> getExerciseUpdatableFromId(Long idExercise) {
        return exerciseDao.getExerciseUpdatableFromId(idExercise);
    }

    public Single<List<ExerciseShort>> getExerciseShortFromId(Set<Long> idExercise) {
        return exerciseDao.getExerciseShortFromId(idExercise);
    }

    public Single<List<Long>> insertMuscles(List<MuscleEntity> muscles) {
        return Single.fromCallable(() -> profileDao.insertMuscles(muscles));
    }

    public Single<List<Long>> insertMuscleGroups(List<MuscleGroupEntity> groups) {
        return Single.fromCallable(() -> profileDao.insertMuscleGroups(groups));
    }

    public Single<Long> createWorkoutProgram() {
        return Single.fromCallable(() -> workoutDao.createWorkoutProgram(new WorkoutProgram()));
    }

    public Single<Routine> insertRoutineForWorkout(@NonNull Long idWorkout) {
        return Single.fromCallable(() -> routineDao.insertRoutineForWorkout(idWorkout));
    }
}
