/*
 * Created by Longphi Nguyen on 2/3/19 11:02 AM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/3/19 11:02 AM.
 */

package com.longlife.workoutlogger.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = WorkoutProgram.class, parentColumns = "idWorkout", childColumns = "idWorkout", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Routine.class, parentColumns = "idRoutine", childColumns = "idRoutine", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {"idWorkout", "idRoutine"}),
                @Index(value = {"idWorkout"}),
                @Index(value = {"idRoutine"})
        }
)
public class WorkoutRoutine {
    @PrimaryKey
    private Long idWorkoutRoutine;
    private Long idWorkout;
    private Long idRoutine;

    public WorkoutRoutine(Long idWorkoutRoutine, Long idWorkout) {
        this.idWorkoutRoutine = idWorkoutRoutine;
        this.idWorkout = idWorkout;
    }

    public Long getIdWorkoutRoutine() {
        return idWorkoutRoutine;
    }

    public void setIdWorkoutRoutine(Long idWorkoutRoutine) {
        this.idWorkoutRoutine = idWorkoutRoutine;
    }

    public Long getIdWorkout() {
        return idWorkout;
    }

    public void setIdWorkout(Long idWorkout) {
        this.idWorkout = idWorkout;
    }

    public Long getIdRoutine() {
        return idRoutine;
    }

    public void setIdRoutine(Long idRoutine) {
        this.idRoutine = idRoutine;
    }
}
