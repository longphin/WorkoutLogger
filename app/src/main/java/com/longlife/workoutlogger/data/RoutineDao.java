package com.longlife.workoutlogger.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.longlife.workoutlogger.enums.PerformanceStatus;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;



/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public abstract class RoutineDao {
    @Query("SELECT * FROM Routine WHERE hidden = 0")
    // Get list of routines.
    public abstract Single<List<Routine>> getRoutines();

    @Query("SELECT * FROM RoutineSession WHERE idRoutine = :idRoutine AND performanceStatus <> 2" + // 2 for PerformanceStatus.COMPLETE
            " ORDER BY sessionDate DESC LIMIT 1")
    // Get the latest routine session for a given routine that has not been performed yet. May not return one.
    public abstract Maybe<RoutineSession> getLatestRoutineSession(Long idRoutine);

    @Query("SELECT * FROM SessionExercise WHERE idRoutineSession = :idRoutineSession")
    // Get all sessions exercises for a given routine.
    public abstract Single<List<SessionExercise>> getSessionExercises(Long idRoutineSession);

    @Query("SELECT e.*" +
            " FROM SessionExercise as se" +
            " INNER JOIN Exercise as e on se.idExercise=e.idExercise" +
            " WHERE se.idSessionExercise = :idSessionExercise")
    // Get exercises for a given session.
    public abstract Single<Exercise> getExerciseFromSession(Long idSessionExercise);

    @Query("SELECT * FROM Routine WHERE idRoutine = :idRoutine")
    // Get a routine.
    public abstract Single<Routine> getRoutine(Long idRoutine);

    // Inserts routine, routine history, routine session, session exercises, session exercise sets. Returns the updated routine.
    @Transaction
    public Routine insertRoutineFull(Routine r, List<RoutineExerciseHelper> reh) {
        // Insert routine.
        Long idRoutine = insertRoutine(r);
        r.setIdRoutine(idRoutine);

        // Insert routine session using the new routine history id.
        RoutineSession routineSessionToAdd = new RoutineSession();
        routineSessionToAdd.setIdRoutine(idRoutine);
        Long idRoutineSession = insertRoutineSession(routineSessionToAdd);

        // Insert exercises for the session using the new session.
        for (int i = 0; i < reh.size(); i++) {
            Long idSessionExercise = insertSessionExercise(new SessionExercise(reh.get(i).getExercise().getIdExerciseLeaf(), idRoutineSession));

            // Insert sets for each exercise added.
            List<SessionExerciseSet> setsToAdd = reh.get(i).getSets();
            for (int j = 0; j < setsToAdd.size(); j++) {
                setsToAdd.get(j).setIdSessionExercise(idSessionExercise);
            }
            insertSessionExerciseSets(setsToAdd);
        }
        return r;
    }

    @Transaction
    // Insert a session for an exercise.
    public SessionExercise insertNewSessionForExercise(Long idExerciseHistory) {
        // Insert new routine session.
        RoutineSession routineSessionToAdd = new RoutineSession();
        routineSessionToAdd.setSessionDateNow(); // Set the session date as current time.
        routineSessionToAdd.setPerformanceStatus(PerformanceStatus.INCOMPLETE); // Initialize the session as incomplete.
        Long idRoutineSession = insertRoutineSession(routineSessionToAdd);
        routineSessionToAdd.setIdRoutineSession(idRoutineSession); // Update session with the new id.

        // Insert SessionExercise.
        SessionExercise newSessionExercise = new SessionExercise(idExerciseHistory, idRoutineSession);
        Long idSessionExercise = insertSessionExercise(newSessionExercise);
        newSessionExercise.setIdSessionExercise(idSessionExercise); // Update session with the new id.

        // Insert a default set. // [TODO] Instead of defaulting to 1 set, include the number of sets in latest performed session (or 1 if no such session).
        insertSessionExerciseSet(new SessionExerciseSet(idSessionExercise));

        return newSessionExercise;
    }

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    // Insert routine.
    public abstract Long insertRoutine(Routine r);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // Insert a session.
    public abstract Long insertRoutineSession(RoutineSession rs);

    @Insert
    // Insert a session exercise.
    public abstract Long insertSessionExercise(SessionExercise se);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // Insert sets.
    public abstract void insertSessionExerciseSets(List<SessionExerciseSet> ses);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // Insert set.
    public abstract void insertSessionExerciseSet(SessionExerciseSet ses);

    @Delete
    // Delete routine.
    public abstract void deleteRoutine(Routine r);

    @Delete
    // Delete session.
    public abstract void deleteRoutineSession(RoutineSession rs);

    @Delete
    // Delete session exercise.
    public abstract void deleteSessionExercise(SessionExercise se);

    @Delete
    // Delete set.
    public abstract void deleteSessionExerciseSet(SessionExerciseSet ses);

    @Query("UPDATE Routine SET hidden = :isHidden WHERE idRoutine=:idRoutine")
    // Hide/unhide a routine.
    public abstract void setRoutineAsHidden(Long idRoutine, int isHidden);
}
