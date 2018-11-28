package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;

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


