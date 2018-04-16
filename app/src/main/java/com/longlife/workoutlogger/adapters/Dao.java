package com.longlife.workoutlogger.adapters;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.longlife.workoutlogger.enums.ExerciseRequestCode;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public interface Dao {
    /**
     * Get all Exercises.
     *
     * @return List of all Exercises.
     */
    @Query("SELECT * FROM Exercise")
    List<Exercise> getExercises();

    /**
     * Get all Routines.
     *
     * @return List of all Routines.
     */
    @Query("SELECT * FROM Routine")
    List<Routine> getRoutines();

    @Insert
    void addRoutines(Routine... routines);

    /**
     * Get latest RoutineSession for a given idRoutine
     *
     * @param idRoutine
     * @return
     */
    @Query("SELECT * FROM RoutineSession WHERE idRoutine = :idRoutine LIMIT 1")
    RoutineSession getLatestRoutineSession(int idRoutine);

    /**
     * Get all Exercises linked (via SessionExercise) to the idRoutineSession.
     *
     * @param idRoutineSession
     * @return
     */
    @Query("SELECT * FROM SessionExercise WHERE idRoutineSession = :idRoutineSession")
    List<SessionExercise> getSessionExercises(int idRoutineSession);

    /**
     * Get the Exercise that is linked to the SessionExercise.
     *
     * @param idSessionExercise
     * @return The Exercise linked to sessionExercise.
     */
    @Query("SELECT e.*" +
            " FROM SessionExercise as se" +
            " INNER JOIN Exercise as e on se.idExercise=e.idExercise" +
            " WHERE se.idSessionExercise = :idSessionExercise")
    Exercise getExerciseFromSession(int idSessionExercise);

    /**
     * Save information from an Exercise to the database.
     *
     * @param exerciseToSave
     */
    //void saveExercise(Exercise exerciseToSave);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRoutineSession(RoutineSession rs);
    //long insertRoutineSession(RoutineSession rs); // returns the rowid (not primary key) of the inserted value.

    /**
     * [TODO] split this up into a return function, and then an insert function for the Dao.
     * Create a copy of a RoutineSession. For example, when starting a new session, we would create
     * a copy of the last session.
     *
     * @param routineSession
     * @return A copy of the routineSession.
     */
    RoutineSession createRoutineSessionCopy(RoutineSession routineSession);

    /**
     * Save information from a Routine to the database.
     *
     * @param routineToSave
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveRoutine(Routine routineToSave);

    // {TODO]  Need to check if the below functions are needed.

    /**
     * Get the HashMap for the SessionExerciseSet
     *
     * @return Hashmap of Sets
     */
    HashMap<Integer, List<SessionExerciseSet>> getSessionExerciseSetHash();

    /**
     * Get the List of exercise sets for the session.
     *
     * @param sessionExercise
     * @return
     */
    List<SessionExerciseSet> getSessionExerciseSets(SessionExercise sessionExercise);

    /**
     * Get the latest routine session for the routine. Will create one if there is no non-performed session.
     *
     * @param routine
     * @return
     */
    RoutineSession getOrCreateLatestRoutineSession(Routine routine);

    /**
     * Get the exercise type for the session exercise.
     *
     * @param sessionExerciseSet
     * @return
     */
    ExerciseRequestCode.ExerciseType getExerciseType(SessionExerciseSet sessionExerciseSet);

    /**
     * Get the measurement type for the session exercise.
     *
     * @param sessionExerciseSet
     * @return
     */
    ExerciseRequestCode.MeasurementType getMeasurementType(SessionExerciseSet sessionExerciseSet);

    /**
     *
     */
    void addValueToExerciseSet(SessionExerciseSet ses, int s);
}
