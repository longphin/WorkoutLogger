package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.longlife.workoutlogger.enums.Muscle;
import com.longlife.workoutlogger.enums.MuscleConverter;

@Entity(indices = @Index(value = {"idExercise", "muscle"}))
public class ExerciseMuscle {
    @PrimaryKey
    private Long idExerciseMuscle;
    private Long idExercise;
    @TypeConverters({MuscleConverter.class})
    private Muscle muscle;
    private double contribution; // as a percentage between 0.0 and 1.0

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

    public Muscle getMuscle() {
        return muscle;
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }

    public double getContribution() {
        return contribution;
    }

    public void setContribution(double contribution) {
        this.contribution = contribution;
    }
}
