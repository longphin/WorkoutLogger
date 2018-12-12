/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = @Index(value = {"idExercise", "idMuscle"}))
public class ExerciseMuscle {
    @PrimaryKey
    private Long idExerciseMuscle;
    private Long idExercise;

    private Long idMuscle; // Muscle is an enum, not a table.
    private double contribution = 1; // as a percentage between 0.0 and 1.0

    public ExerciseMuscle() {
    }

    @Ignore
    public ExerciseMuscle(Long idMuscle) {
        this.idMuscle = idMuscle;
    }

    public Long getIdMuscle() {
        return idMuscle;
    }

    public void setIdMuscle(Long idMuscle) {
        this.idMuscle = idMuscle;
    }

    public Long getIdExerciseMuscle() {
        return idExerciseMuscle;
    }

    public void setIdExerciseMuscle(Long idExerciseMuscle) {
        this.idExerciseMuscle = idExerciseMuscle;
    }

    public Long getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
    }

    public double getContribution() {
        return contribution;
    }

    public void setContribution(double contribution) {
        this.contribution = contribution;
    }

    @Override
    public int hashCode() {
        return (int) ((idExercise == null ? 0L : idExercise * 30L) + idMuscle);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        ExerciseMuscle other = (ExerciseMuscle) obj;
        return (idExercise == null && other.getIdExercise() == null) || (idExercise.equals(other.getIdExercise()))
                && idMuscle.equals(other.getIdMuscle());
    }
}
