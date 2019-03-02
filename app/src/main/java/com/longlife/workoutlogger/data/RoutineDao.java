/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/27/18 9:07 PM.
 */

package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.enums.PerformanceStatus;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Routine.Routine;
import com.longlife.workoutlogger.model.Routine.RoutineExercise;
import com.longlife.workoutlogger.model.Routine.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.model.Workout.WorkoutRoutine;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;
import com.longlife.workoutlogger.view.Workout.Create.RoutineAdapter;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import io.reactivex.Maybe;
import io.reactivex.Single;


/**
 * Created by Longphi on 1/3/2018.
 */

@androidx.room.Dao
public abstract class RoutineDao {
    // Get list of routines.
    @Query("SELECT * FROM Routine WHERE hidden = 0")
    public abstract Single<List<Routine>> getRoutines();

    // Get the latest routine session for a given routine that has not been performed yet. May not return one.
    @Query("SELECT * FROM RoutineSession WHERE idRoutine = :idRoutine AND performanceStatus <> 2" + // 2 for PerformanceStatus.COMPLETE
            " ORDER BY sessionDate DESC LIMIT 1")
    public abstract Maybe<RoutineSession> getLatestRoutineSession(Long idRoutine);

    // Get all sessions exercises for a given routine.
    @Query("SELECT * FROM SessionExercise WHERE idRoutineSession = :idRoutineSession")
    public abstract Single<List<SessionExercise>> getSessionExercises(Long idRoutineSession);

    // Get exercises for a given session.
    @Query("SELECT e.*" +
            " FROM SessionExercise as se" +
            " INNER JOIN Exercise as e on se.idExercise=e.idExercise" +
            " WHERE se.idSessionExercise = :idSessionExercise")
    public abstract Single<Exercise> getExerciseFromSession(Long idSessionExercise);

    // Get a routine.
    @Query("SELECT * FROM Routine WHERE idRoutine = :idRoutine")
    public abstract Single<Routine> getRoutine(Long idRoutine);

    // Inserts routine, routine history, routine session, session exercises, session exercise sets. Returns the updated routine.
    @Transaction
    public Routine insertRoutineFull(Routine r, List<RoutineExerciseHelper> reh) {
        // Insert routine.
        Long idRoutine = insertRoutine(r);
        r.setIdRoutine(idRoutine);

        // Insert routine session using the new routine id.
        RoutineSession routineSessionToAdd = new RoutineSession();
        routineSessionToAdd.setIdRoutine(idRoutine);
        Long idRoutineSession = insertRoutineSession(routineSessionToAdd);

        // Insert exercises for the session using the new session.
        for (int i = 0; i < reh.size(); i++) {
            Long idSessionExercise = insertSessionExercise(new SessionExercise(reh.get(i).getExercise().getIdExercise(), idRoutineSession, reh.get(i).getExercise().getNote()));

            // Insert sets for each exercise added.
            List<SessionExerciseSet> setsToAdd = reh.get(i).getSets();
            for (int j = 0; j < setsToAdd.size(); j++) {
                setsToAdd.get(j).setIdSessionExercise(idSessionExercise);
            }
            insertSessionExerciseSets(setsToAdd);
        }
        return r;
    }

    // Insert routine.
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    public abstract Long insertRoutine(Routine r);

    // Insert a session.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insertRoutineSession(RoutineSession rs);

    // Insert a session exercise.
    @Insert
    public abstract Long insertSessionExercise(SessionExercise se);

    // Insert sets.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSessionExerciseSets(List<SessionExerciseSet> ses);

    // Insert a session for an exercise. i.e. performing an exercise outside of a routine.
    @Transaction
    public SessionExercise insertNewSessionForExercise(Long idExercise, String note) {
        // Insert new routine session.
        RoutineSession routineSessionToAdd = new RoutineSession();
        routineSessionToAdd.setSessionDateNow(); // Set the session date as current time.
        routineSessionToAdd.setPerformanceStatus(PerformanceStatus.INCOMPLETE); // Initialize the session as incomplete.
        Long idRoutineSession = insertRoutineSession(routineSessionToAdd);
        routineSessionToAdd.setIdRoutineSession(idRoutineSession); // Update session with the new id.

        // Insert SessionExercise.
        SessionExercise newSessionExercise = new SessionExercise(idExercise, idRoutineSession, note);
        Long idSessionExercise = insertSessionExercise(newSessionExercise);
        newSessionExercise.setIdSessionExercise(idSessionExercise); // Update session with the new id.

        // Insert a default set. // [TODO] Instead of defaulting to 1 set, include the number of sets in latest performed session (or 1 if no such session).
        insertSessionExerciseSet(new SessionExerciseSet(idSessionExercise));

        return newSessionExercise;
    }

    // Insert set.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSessionExerciseSet(SessionExerciseSet ses);

    // Delete routine.
    @Delete
    public abstract void deleteRoutine(Routine r);

    // Delete session.
    @Delete
    public abstract void deleteRoutineSession(RoutineSession rs);

    // Delete session exercise.
    @Delete
    public abstract void deleteSessionExercise(SessionExercise se);

    // Delete set.
    @Delete
    public abstract void deleteSessionExerciseSet(SessionExerciseSet ses);

    // Hide/unhide a routine.
    @Query("UPDATE Routine SET hidden = :isHidden WHERE idRoutine=:idRoutine")
    public abstract void setRoutineAsHidden(Long idRoutine, int isHidden);

    // Create a new routine for a workout. Return the idRoutine for the new routine.
    @Transaction
    public Routine insertRoutineForWorkout(Long idWorkout) {
        Routine routineToAdd = new Routine();
        Long idRoutine = insertRoutine(routineToAdd);
        routineToAdd.setIdRoutine(idRoutine);

        insertWorkoutRoutine(new WorkoutRoutine(idRoutine, idWorkout));

        return routineToAdd;
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insertWorkoutRoutine(WorkoutRoutine workoutRoutine);

    @Query("SELECT r.*" +
            " FROM WorkoutRoutine AS wr" +
            "   LEFT JOIN Routine AS r ON wr.idRoutine=r.idRoutine" +
            " WHERE wr.idWorkout=:idWorkout")
    public abstract Single<List<Routine>> getRoutinesForWorkout(Long idWorkout);

    @Transaction
    public RoutineAdapter.exerciseItemInRoutine insertExerciseIntoRoutine(RoutineAdapter.exerciseItemInRoutine ex) {
        Long idRoutineExercise = insertRoutineExercise(new RoutineExercise(ex.getIdRoutine(), ex.getIdExercise(), ex.getNumberOfSets()));

        ex.setIdRoutineExercise(idRoutineExercise);
        return ex;
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insertRoutineExercise(RoutineExercise routineExercise);

    @Query("SELECT re.idRoutineExercise, r.idRoutine, e.idExercise, e.name, re.numberOfSets" +
            " FROM Routine as r" +
            " INNER JOIN RoutineExercise as re on r.idRoutine=re.idRoutine" +
            " INNER JOIN Exercise as e on re.idExercise=e.idExercise" +
            " WHERE r.idRoutine=:idRoutine")
    public abstract Single<List<RoutineAdapter.exerciseItemInRoutine>> getExercisesShortForRoutine(Long idRoutine);
}
