/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/22/18 10:16 PM.
 */

package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;

import java.util.List;

// POJO class for obtaining a session exercise with related sets.
public class ExerciseSessionWithSets {
    @Embedded
    public SessionExercise sessionExercise;

    // Need to add Exercise object POJO that goes through Exercise. Or change RoutineExerciseHelper to a POJO (may be easier).
    @Relation(parentColumn = "idExercise", entityColumn = "idExercise", entity = Exercise.class)
    public List<ExerciseShort> exercise;

    @Relation(parentColumn = "idSessionExercise", entityColumn = "idSessionExercise", entity = SessionExerciseSet.class)
    public List<SessionExerciseSet> sets;

    public SessionExercise getSessionExercise() {
        return sessionExercise;
    }

    public ExerciseShort getExercise() {
        return exercise.get(0);
    }

    public List<SessionExerciseSet> getSets() {
        return sets;
    }
}
