/*
 * Created by Longphi Nguyen on 2/18/19 2:36 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/3/19 12:35 PM.
 */

package com.longlife.workoutlogger.model.Routine;

import com.longlife.workoutlogger.model.Exercise.Exercise;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Routine.class, parentColumns = "idRoutine", childColumns = "idRoutine"),
        @ForeignKey(entity = Exercise.class, parentColumns = "idExercise", childColumns = "idExercise")
},
        indices = {
                @Index(value = {"idRoutine", "idExercise"}),
                @Index(value = {"idRoutine"}),
                @Index(value = {"idExercise"})
        }
)
public class RoutineExercise {
    @PrimaryKey
    private Long idRoutineExercise;
    private Long idRoutine;
    private Long idExercise;

    public Long getIdRoutineExercise() {
        return idRoutineExercise;
    }

    public void setIdRoutineExercise(Long idRoutineExercise) {
        this.idRoutineExercise = idRoutineExercise;
    }

    public Long getIdRoutine() {
        return idRoutine;
    }

    public void setIdRoutine(Long idRoutine) {
        this.idRoutine = idRoutine;
    }

    public Long getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
    }
}
