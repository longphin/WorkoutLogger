package com.longlife.workoutlogger.model;

import java.util.List;

/**
 * Created by Longphi on 1/3/2018.
 */

public interface DataAccessorInterface {
    List<Exercise> getExercises();
    List<Routine> getRoutines();
    int getLatestRoutineSession(int idRoutine);
    List<SessionExercise> getSessionExercises(int idRoutineSession);
    Exercise getExerciseFromSession(int idSessionExercise);
    void saveExercise(Exercise exercise);
}
