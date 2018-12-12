/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 7:46 PM.
 */

package com.longlife.workoutlogger.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.Exercise.ExerciseWithMuscleGroup;
import com.longlife.workoutlogger.model.ExerciseMuscle;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.SessionExercise;

import java.util.List;
import java.util.Set;

import io.reactivex.Maybe;
import io.reactivex.Single;


/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public abstract class ExerciseDao {
    // Get a list of exercises that are not hidden.
    @Query("SELECT * FROM Exercise WHERE hidden = 0")
    public abstract Single<List<Exercise>> getExercises();

    // Get a list of exercises (short) that are not hidden.
    @Query("SELECT idExercise, name, note, locked FROM Exercise WHERE hidden=0")
    public abstract Single<List<ExerciseShort>> getExerciseShort();

    // Get a list of exercises, with the related muscles split up (1 row per muscle), for a specified muscle group.
    @Query("SELECT e.idExercise, e.name, e.note, e.locked, em.idMuscle " +
            "FROM Exercise as e " +
            "   LEFT JOIN ExerciseMuscle as em on e.idExercise=em.idExercise " +
            "   LEFT JOIN MuscleEntity as me on em.idMuscle=me.idMuscle " +
            "WHERE hidden=0 AND me.idMuscleGroup=:idMuscleGroup")
    public abstract Single<List<ExerciseWithMuscleGroup>> getExerciseByMuscleGroup(int idMuscleGroup);

    // Get the name of exercises that are not hidden.
    @Query("SELECT Name FROM Exercise WHERE hidden = 0")
    public abstract Single<List<String>> getExercisesNames();

    // Check if an exercise with the given name already exists.
    @Query("SELECT * FROM Exercise WHERE name = :name")
    public abstract Single<Exercise> getExercise(String name);

    // Check if an exercise with the given id already exists.
    @Query("SELECT EXISTS (SELECT 1 FROM Exercise WHERE idExercise = :idExercise)")
    public abstract Single<Integer> exerciseExists(Long idExercise);

    // Get a list of exercises given a list of idExercises.
    @Query("SELECT * FROM Exercise WHERE idExercise IN (:ids)")
    public abstract Single<List<Exercise>> getExerciseFromId(Set<Long> ids);

    // Get a single exercise given the id.
    @Query("SELECT * FROM Exercise WHERE idExercise=:id")
    public abstract Single<Exercise> getExerciseFromId(Long id);

    // Get a subset of columns from exercise that can be edited.
    @Transaction
    @Query("SELECT idExercise, name, note, exerciseType FROM Exercise WHERE idExercise=:id")
    public abstract Single<ExerciseUpdated> getExerciseUpdatableFromId(Long id);

    // Get a subset of columns from exercise that is minimal.
    @Query("SELECT idExercise, name, note, locked FROM Exercise WHERE idExercise=:id")
    public abstract Single<ExerciseShort> getExerciseShortFromId(Long id);

    // Get a subset of columns from exercise that is minimal.
    @Query("SELECT idExercise, name, note, locked FROM Exercise WHERE idExercise IN (:ids)")
    public abstract Single<List<ExerciseShort>> getExerciseShortFromId(Set<Long> ids);

    // Update the lock status of an exercise.
    @Query("UPDATE Exercise SET locked = :lockedStatus WHERE idExercise = :idExercise")
    public abstract void updateLockedStatus(Long idExercise, boolean lockedStatus);

    // Get session exercise with related sets.
    @Transaction
    @Query("SELECT * FROM SessionExercise WHERE idSessionExercise=:idSessionExercise")
    public abstract Single<ExerciseSessionWithSets> getSessionExerciseWithSets(Long idSessionExercise);

    // Get the latest unperformed session for an exercise.
    @Query("SELECT se.*" +
            "FROM Exercise as e" +
            " INNER JOIN SessionExercise as se ON se.idExercise=e.idExercise" +
            " INNER JOIN RoutineSession as rs ON rs.idRoutineSession=se.idRoutineSession " +
            "WHERE e.idExercise=:idExercise " +
            "AND rs.performanceStatus=0 " + // Only look for new sessions, so we do not have to recreate a new session. // [TODO] Figure out a way to not hard-code this value. Get it from the enum.
            "AND rs.idRoutine IS NULL " + // Only look for sessions specifically for this exercise, not related to a routine.
            "LIMIT 1")
    public abstract Maybe<SessionExercise> getLatestExerciseSession(Long idExercise);

    // Update an exercise.
    @Update
    public abstract void updateExercise(Exercise ex);

    // Inserts an exercise. Returns the exercise with provided id.
    @Transaction
    public Exercise insertExerciseFull(Exercise ex, Set<ExerciseMuscle> relatedMuscles) {
        Long idExercise = insertExercise(ex);
        ex.setIdExercise(idExercise);

        for (ExerciseMuscle muscle : relatedMuscles) {
            muscle.setIdExercise(idExercise);
        }
        insertExerciseMuscles(relatedMuscles);

        return ex;
    }

    @Insert
    public abstract List<Long> insertExerciseMuscles(Set<ExerciseMuscle> muscles);

    // Insert an exercise. Do not use directly. Instead, use insertExerciseFull(exercise) to insert a source exercise and a leaf copy.
    @Insert(onConflict = OnConflictStrategy.REPLACE)//OnConflictStrategy.ROLLBACK)
    public abstract Long insertExercise(Exercise ex);

    // Delete an exercise. Currently, we do not use this because exercises will only be hidden/unhidden.
    @Delete
    public abstract void deleteExercise(Exercise ex);

    // Hide/unhide an exercise.
    @Query("UPDATE Exercise SET hidden = :isHidden WHERE idExercise = :idExercise")
    public abstract void setExerciseHiddenStatus(Long idExercise, int isHidden); // isHidden = 1 for hidden, 0 for not hidden

    @Transaction
    public void updateExercise(ExerciseUpdated ex, Set<ExerciseMuscle> relatedMuscles, Set<ExerciseMuscle> musclesToDelete) {
        updateExercise(ex.getIdExercise(), ex.getName(), ex.getNote(), ex.getExerciseType());

        deleteExerciseMuscles(musclesToDelete);
        insertExerciseMuscles(relatedMuscles);
    }

    @Query("UPDATE Exercise " +
            "SET name = :name " +
            ", note = :note " +
            ", exerciseType = :exerciseType " +
            "WHERE idExercise = :idExercise")
    public abstract void updateExercise(Long idExercise, String name, String note, int exerciseType);

    @Delete
    abstract void deleteExerciseMuscles(Set<ExerciseMuscle> musclesToDelete);
}
