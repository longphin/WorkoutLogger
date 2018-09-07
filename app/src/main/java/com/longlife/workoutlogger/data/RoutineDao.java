package com.longlife.workoutlogger.data;

import android.arch.persistence.room.*;
import com.longlife.workoutlogger.model.*;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;



/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public abstract class RoutineDao {
    @Query("SELECT * FROM Routine WHERE hidden = 0")
    // Get list of routines.
    public abstract Single<List<Routine>> getRoutines();

    @Query("SELECT * FROM RoutineSession WHERE idRoutineHistory = :idRoutine AND performanceStatus <> 2" + // 2 for PerformanceStatus.COMPLETE
            " ORDER BY sessionDate DESC LIMIT 1")
    // Get the latest routine session for a given routine that has not been performed yet. May not return one.
    public abstract Maybe<RoutineSession> getLatestRoutineSession(Long idRoutine);

    @Query("SELECT * FROM SessionExercise WHERE idRoutineSession = :idRoutineSession")
    // Get all sessions exercises for a given routine.
    public abstract Single<List<SessionExercise>> getSessionExercises(Long idRoutineSession);

    @Query("SELECT e.*" +
            " FROM SessionExercise as se" +
            " INNER JOIN Exercise as e on se.idExerciseHistory=e.idExercise" +
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

        // Insert this newly created routine to history.
        Long idRoutineHistory = insertRoutineHistory(new RoutineHistory(r));
        r.setCurrentIdRoutineHistory(idRoutineHistory);
        updateIdHistory(idRoutine, idRoutineHistory);

        // Insert routine session using the new routine history id.
        RoutineSession routineSessionToAdd = new RoutineSession();
        routineSessionToAdd.setIdRoutineHistory(idRoutineHistory);
        Long idRoutineSession = insertRoutineSession(routineSessionToAdd);

        // Insert exercises for the session using the new session.
        for (int i = 0; i < reh.size(); i++) {
            Long idSessionExercise = insertSessionExercise(new SessionExercise(reh.get(i).getExercise().getCurrentIdExerciseHistory(), idRoutineSession));

            // Insert sets for each exercise added.
            List<SessionExerciseSet> setsToAdd = reh.get(i).getSets();
            for (int j = 0; j < setsToAdd.size(); j++) {
                setsToAdd.get(j).setIdSessionExercise(idSessionExercise);
            }
            insertSessionExerciseSets(setsToAdd);
        }
        return r;
    }

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    // Insert routine.
    public abstract Long insertRoutine(Routine r);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    // Insert routine history.
    public abstract Long insertRoutineHistory(RoutineHistory rh);

    @Query("UPDATE Routine SET currentIdRoutineHistory = :idRoutineHistory WHERE idRoutine = :idRoutine")
    // Update the history id that a routine points to. This is to keep the history id to the most current one.
    public abstract void updateIdHistory(Long idRoutine, Long idRoutineHistory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // Insert a session.
    public abstract Long insertRoutineSession(RoutineSession rs);

    @Insert
    // Insert a session exercise.
    public abstract Long insertSessionExercise(SessionExercise se);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // Insert sets.
    public abstract void insertSessionExerciseSets(List<SessionExerciseSet> ses);

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
