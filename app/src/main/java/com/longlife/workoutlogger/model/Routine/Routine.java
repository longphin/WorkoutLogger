/*
 * Created by Longphi Nguyen on 2/18/19 2:36 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/13/19 8:05 AM.
 */

package com.longlife.workoutlogger.model.Routine;

import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import io.reactivex.annotations.NonNull;

/**
 * This will be the Routine object.
 */
@Entity
public class Routine {
    // Incremented value to ensure each Routine gets a unique Id.
    @PrimaryKey
    @NonNull
    private Long idRoutine;
    @Required
    private String name;
    // Note for routine.
    private String note;
    // Flag for hiding the routine.
    @NonNull
    private boolean hidden = false;

    private Long idWorkoutProgram;

    public Long getIdWorkoutProgram() {
        return idWorkoutProgram;
    }

    public void setIdWorkoutProgram(Long idWorkoutProgram) {
        this.idWorkoutProgram = idWorkoutProgram;
    }

    public Routine() {

    }

    public Long getIdRoutine() {
        return idRoutine;
    }

    public void setIdRoutine(Long idRoutine) {
        this.idRoutine = idRoutine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

}


