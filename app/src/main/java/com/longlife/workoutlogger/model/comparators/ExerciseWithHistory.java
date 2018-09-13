package com.longlife.workoutlogger.model.comparators;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseHistory;

public class ExerciseWithHistory {
    @Embedded
    public ExerciseHistory exerciseHistory;

    @Relation(parentColumn = "idExercise", entityColumn = "idExercise", entity = Exercise.class)
    public Exercise exercise;
}
