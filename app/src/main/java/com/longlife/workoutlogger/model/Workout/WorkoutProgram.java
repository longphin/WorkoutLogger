/*
 * Created by Longphi Nguyen on 2/18/19 2:37 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/16/19 5:06 PM.
 */

package com.longlife.workoutlogger.model.Workout;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WorkoutProgram {
    @PrimaryKey
    private Long idWorkout;
    private boolean isActive = false;
    private String name;

    private boolean isSaved = false;

    public boolean isSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean saved) {
        isSaved = saved;
    }

    public Long getIdWorkout() {
        return idWorkout;
    }

    public void setIdWorkout(Long idWorkout) {
        this.idWorkout = idWorkout;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
