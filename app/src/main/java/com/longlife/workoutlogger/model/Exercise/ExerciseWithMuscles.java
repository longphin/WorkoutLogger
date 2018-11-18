package com.longlife.workoutlogger.model.Exercise;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.longlife.workoutlogger.model.ExerciseMuscle;

import java.util.List;

public class ExerciseWithMuscles {
    @Embedded
    private Exercise exercise;
    @Relation(parentColumn = "idExercise", entityColumn = "idExercise", entity = ExerciseMuscle.class)
    private List<ExerciseMuscle> muscles;

    public Exercise getExercise() {
        return exercise;
    }

    public List<ExerciseMuscle> getMuscles() {
        return muscles;
    }
}
