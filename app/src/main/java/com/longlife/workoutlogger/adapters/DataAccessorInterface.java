package com.longlife.workoutlogger.adapters;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.v2.enums.ExerciseType;
import com.longlife.workoutlogger.v2.enums.MeasurementType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Longphi on 1/3/2018.
 */

public interface DataAccessorInterface {
    /**
     * Get all Exercises.
     *
     * @return List of all Exercises.
     */
    List<Exercise> getExercises();

    /**
     * Get all Routines.
     *
     * @return List of all Routines.
     */
    List<Routine> getRoutines();

    /** Get latest RoutineSession for a given Routine.
     * @param routine
     * @return The last RoutineSession for the routine.
     */
    RoutineSession getLatestRoutineSession(Routine routine);

    /** Get all Exercises linked (via SessionExercise) to the RoutineSession.
     * @param routineSession
     * @return A list of SessionExercises linked to the routineSession.
     */
    List<SessionExercise> getSessionExercises(RoutineSession routineSession);

    /** Get the Exercise that is linked to the SessionExercise.
     * @param sessionExercise
     * @return The Exercise linked to sessionExercise.
     */
    Exercise getExerciseFromSession(SessionExercise sessionExercise);

    /** Save information from an Exercise to the database.
     * @param exerciseToSave
     */
    void saveExercise(Exercise exerciseToSave);

    /** Create a copy of a RoutineSession. For example, when starting a new session, we would create
     * a copy of the last session.
     * @param routineSession
     * @return A copy of the routineSession.
     */
    RoutineSession createRoutineSessionCopy(RoutineSession routineSession);

    /** Save information from a Routine to the database.
     * @param routineToSave
     */
    void saveRoutine(Routine routineToSave);

    /**
     * Get the HashMap for the SessionExerciseSet
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
     * @param routine
     * @return
     */
    RoutineSession getOrCreateLatestRoutineSession(Routine routine);

    /**
     * Get the exercises type for the session exercises.
     *
     * @param sessionExerciseSet
     * @return
     */
    ExerciseType getExerciseType(SessionExerciseSet sessionExerciseSet);

    /**
     * Get the measurement type for the session exercise.
     *
     * @param sessionExerciseSet
     * @return
     */
    MeasurementType getMeasurementType(SessionExerciseSet sessionExerciseSet);

    /**
     *
     */
    void addValueToExerciseSet(SessionExerciseSet ses, int s);
}
