/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:22 PM.
 */

package com.longlife.workoutlogger.model.Exercise;

import com.longlife.workoutlogger.model.ExerciseMuscle;

import java.util.Set;

import androidx.room.Relation;

public class ExerciseUpdated {
    private Long idExercise;
    private String name;
    private String note;

    private int exerciseType;
    @Relation(parentColumn = "idExercise", entityColumn = "idExercise", entity = ExerciseMuscle.class)
    private Set<ExerciseMuscle> muscles;

    public ExerciseUpdated() {
    }

    public ExerciseUpdated(Exercise newExercise) {
        this.idExercise = newExercise.getIdExercise();
        this.name = newExercise.getName();
        this.note = newExercise.getNote();
    }

    public int getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(int exerciseType) {
        this.exerciseType = exerciseType;
    }

    public Set<ExerciseMuscle> getMuscles() {
        return muscles;
    }

    public void setMuscles(Set<ExerciseMuscle> muscles) {
        this.muscles = muscles;
    }

    public Long getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
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
}
