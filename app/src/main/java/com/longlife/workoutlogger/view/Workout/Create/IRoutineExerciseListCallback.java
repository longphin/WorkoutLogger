/*
 * Created by Longphi Nguyen on 3/19/19 7:25 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/19/19 7:25 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;

public interface IRoutineExerciseListCallback {
    void routineExerciseDelete(Long idRoutineExercise);

    void routineExerciseEdit(Long idRoutineExercise);
}
