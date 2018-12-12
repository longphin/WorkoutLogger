/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/22/18 10:16 PM.
 */

package com.longlife.workoutlogger.model.Exercise;

// Helper class for when an exercise's lock status is changed.
public class ExerciseLocked {
    private Long idExercise;
    private boolean isLocked;

    public ExerciseLocked(Long idExercise, boolean isLocked) {
        this.idExercise = idExercise;
        this.isLocked = isLocked;
    }


    public Long getIdExercise() {
        return idExercise;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
