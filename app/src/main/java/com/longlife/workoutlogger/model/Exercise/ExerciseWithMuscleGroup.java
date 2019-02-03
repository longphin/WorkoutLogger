/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 8:06 PM.
 */

package com.longlife.workoutlogger.model.Exercise;

import androidx.room.Ignore;

public class ExerciseWithMuscleGroup implements IExerciseListable {
    public Long idExercise;
    public String name;
    public String note;
    private boolean locked; // Flag to indicate whether exercise is locked.
    @Ignore
    private String muscleName;
    private int idMuscle;

    public ExerciseWithMuscleGroup(Long idExercise, String name, String note, boolean locked, int idMuscle) {
        this.idExercise = idExercise;
        this.name = name;
        this.note = note;
        this.locked = locked;
        this.idMuscle = idMuscle;
    }

    public String getMuscleName() {
        return muscleName;
    }

    public void setMuscleName(String muscleName) {
        this.muscleName = muscleName;
    }

    public int getIdMuscle() {
        return idMuscle;
    }

    public void setIdMuscle(int idMuscle) {
        this.idMuscle = idMuscle;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
    }

    @Override
    public Long getIdExercise() {
        return idExercise;
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

    public boolean isLocked() {
        return locked;
    }

    @Override
    @Ignore
    public void update(ExerciseUpdated updatedExercise) {
        this.idExercise = updatedExercise.getIdExercise();
        this.name = updatedExercise.getName();
        this.note = updatedExercise.getNote();
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    @Ignore
    public String getCategory() {
        return muscleName;
    }
}
