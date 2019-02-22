/*
 * Created by Longphi Nguyen on 2/18/19 2:37 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/16/19 5:06 PM.
 */

package com.longlife.workoutlogger.model.Workout;

import com.longlife.workoutlogger.model.Routine.Routine;

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

    public WorkoutRoutine(Long idRoutine, Long idWorkout) {
        this.idRoutine = idRoutine;
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
