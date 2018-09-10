package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

// POJO class for obtaining a session exercise with related sets.
public class ExerciseSessionWithSets {
    @Embedded
    public SessionExercise sessionExercise;

    @Relation(parentColumn = "idSessionExercise", entityColumn = "idSessionExercise", entity = SessionExerciseSet.class)
    public List<SessionExerciseSet> sets;
}
