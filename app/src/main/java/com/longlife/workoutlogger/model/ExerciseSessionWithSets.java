package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

// POJO class for obtaining a session exercise with related sets.
public class ExerciseSessionWithSets {
    @Embedded
    public SessionExercise sessionExercise;

    // Need to add Exercise object POJO that goes through Exercise. Or change RoutineExerciseHelper to a POJO (may be easier).
    @Relation(parentColumn = "idExercise", entityColumn = "idExercise", entity = Exercise.class)
    public List<Exercise> exercise;

    @Relation(parentColumn = "idSessionExercise", entityColumn = "idSessionExercise", entity = SessionExerciseSet.class)
    public List<SessionExerciseSet> sets;
}
