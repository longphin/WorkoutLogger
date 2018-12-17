/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 7:36 PM.
 */

package com.longlife.workoutlogger.model.Exercise;

public interface IExerciseListable {
    String getNote();

    String getName();

    Long getIdExercise();

    void update(ExerciseUpdated updatedExercise);

    void setLocked(boolean locked);

    String getCategory();
}
