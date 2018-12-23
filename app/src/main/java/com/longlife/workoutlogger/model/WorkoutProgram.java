/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/27/18 9:07 PM.
 */

package com.longlife.workoutlogger.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WorkoutProgram {
    @PrimaryKey
    private Long idWorkoutProgram;
    private boolean isActive = false;
    private String name;

    public Long getIdWorkoutProgram() {
        return idWorkoutProgram;
    }

    public void setIdWorkoutProgram(Long idWorkoutProgram) {
        this.idWorkoutProgram = idWorkoutProgram;
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
