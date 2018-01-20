package com.longlife.workoutlogger.model;

import java.util.List;

/**
 * Created by Longphi on 1/3/2018.
 */

public interface DataAccessorInterface {
    List<Exercise> getExercises();
    List<Routine> getRoutines();
    //int getLatestIdRoutineSession(int idRoutine);
    //RoutineSession getLatestRoutineSession(int idRoutine);
    RoutineSession getLatestRoutineSession(Routine routine);
    //List<SessionExercise> getSessionExercises(int idRoutineSession);
    List<SessionExercise> getSessionExercises(RoutineSession routineSession);
    //Exercise getExerciseFromSession(int idSessionExercise);
    Exercise getExerciseFromSession(SessionExercise sessionExercise);
    void saveExercise(Exercise exercise);
    SessionExercise createBlankSessionExercise(RoutineSession sessionToAddTo);

    RoutineSession createRoutineSessionCopy(RoutineSession routineSession);
    void deleteRoutineSession(RoutineSession routineSession);
}
