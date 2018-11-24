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
}
