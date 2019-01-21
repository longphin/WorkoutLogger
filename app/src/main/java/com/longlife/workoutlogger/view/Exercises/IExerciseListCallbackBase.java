/*
 * Created by Longphi Nguyen on 1/20/19 5:43 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/20/19 5:43 PM.
 */

package com.longlife.workoutlogger.view.Exercises;

import android.content.Context;

public interface IExerciseListCallbackBase {
    // When an exercise is clicked, send the clicked exercise.
    void exerciseEdit(Long idExercise);

    Context getContext();
}
