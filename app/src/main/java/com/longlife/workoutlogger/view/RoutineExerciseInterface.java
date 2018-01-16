package com.longlife.workoutlogger.view;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.SessionExercise;

import java.util.List;

/**
 * Created by Longphi on 1/6/2018.
 */

public interface RoutineExerciseInterface {
    void startExerciseActivity(Exercise exercise);

    void setUpAdapterAndView(List<SessionExercise> sessionExercises);
}
