package com.longlife.workoutlogger.model;

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

    List<SessionExerciseSet> getSessionExerciseSets(SessionExercise sessionExercise);
    //void deleteRoutineSession(RoutineSession routineSession); // [TODO] remove
}
