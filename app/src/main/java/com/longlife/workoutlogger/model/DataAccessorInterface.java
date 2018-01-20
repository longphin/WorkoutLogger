package com.longlife.workoutlogger.model;

import java.util.List;

/**
 * Created by Longphi on 1/3/2018.
 */

public interface DataAccessorInterface {
    List<Exercise> getExercises();
    List<Routine> getRoutines();
    RoutineSession getLatestRoutineSession(Routine routine);
    List<SessionExercise> getSessionExercises(RoutineSession routineSession);
    Exercise getExerciseFromSession(SessionExercise sessionExercise);

    void saveExercise(Exercise exerciseToSave);

    RoutineSession createRoutineSessionCopy(RoutineSession routineSession);

    //void deleteRoutineSession(RoutineSession routineSession); // [TODO] remove
    void saveRoutine(Routine routineToSave);
}
