/*
 * Created by Longphi Nguyen on 3/25/19 9:21 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/25/19 9:21 PM.
 */

package com.longlife.workoutlogger.model.Routine;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = RoutineExercise.class, parentColumns = "idRoutineExercise", childColumns = "idRoutineExercise"),
        indices = {
                @Index(value = {"idRoutineExercise"}),
                @Index(value = {"idRoutineExercise", "order"})
        })
public class ExerciseSet {
    @PrimaryKey
    private Long idExerciseSet;
    private Long idRoutineExercise;

    private int order;

    @Ignore
    public ExerciseSet() {

    }

    public ExerciseSet(Long idRoutineExercise) {
        this.idRoutineExercise = idRoutineExercise;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Long getIdExerciseSet() {
        return idExerciseSet;
    }

    public void setIdExerciseSet(Long idExerciseSet) {
        this.idExerciseSet = idExerciseSet;
    }

    public Long getIdRoutineExercise() {
        return idRoutineExercise;
    }

    public void setIdRoutineExercise(Long idRoutineExercise) {
        this.idRoutineExercise = idRoutineExercise;
    }
}
