/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 7:36 PM.
 */

package com.longlife.workoutlogger.model.Exercise;

public class ExerciseShort implements IExerciseListable {
    public Long idExercise;
    public String name;
    public String note;
    private boolean locked; // Flag to indicate whether exercise is locked.

    public ExerciseShort(Long idExercise, String name, String note, boolean locked) {
        this.idExercise = idExercise;
        this.name = name;
        this.note = note;
        this.locked = locked;
    }

    // Constructor to get an ExerciseShort from an Exercise.
    public ExerciseShort(Exercise ex) {
        this.idExercise = ex.getIdExercise();
        this.name = ex.getName();
        this.note = ex.getNote();
        this.locked = ex.isLocked();
    }

    // Constructor for when an exercise is updated.
    public ExerciseShort(ExerciseUpdated ex) {
        update(ex);
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String getCategory() {
        return name.substring(0, Math.min(1, name.length())).toUpperCase();
    }

    public String getNote() {
        return note;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getIdExercise() {
        return idExercise;
    }

    @Override
    public void update(ExerciseUpdated updatedExercise) {
        this.idExercise = updatedExercise.getIdExercise();
        this.name = updatedExercise.getName();
        this.note = updatedExercise.getNote();
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }
    /*
    public ExerciseShort update(ExerciseUpdated updatedExercise) {
        updateMe(updatedExercise);
        return(this);
    }
    */
}
