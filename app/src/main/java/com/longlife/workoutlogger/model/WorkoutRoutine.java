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
                @ForeignKey(entity = WorkoutProgram.class, parentColumns = "idWorkoutProgram", childColumns = "idWorkoutProgram", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Routine.class, parentColumns = "idRoutine", childColumns = "idRoutine", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {"idWorkoutProgram", "idRoutine"}),
                @Index(value = {"idWorkoutProgram"}),
                @Index(value = {"idRoutine"})
        }
)
public class WorkoutRoutine {
    @PrimaryKey
    private Long idWorkoutRoutine;
    private Long idWorkoutProgram;
    private Long idRoutine;

    public WorkoutRoutine(Long idWorkoutRoutine, Long idWorkoutProgram) {
        this.idWorkoutRoutine = idWorkoutRoutine;
        this.idWorkoutProgram = idWorkoutProgram;
    }

    public Long getIdWorkoutRoutine() {
        return idWorkoutRoutine;
    }

    public void setIdWorkoutRoutine(Long idWorkoutRoutine) {
        this.idWorkoutRoutine = idWorkoutRoutine;
    }

    public Long getIdWorkoutProgram() {
        return idWorkoutProgram;
    }

    public void setIdWorkoutProgram(Long idWorkoutProgram) {
        this.idWorkoutProgram = idWorkoutProgram;
    }

    public Long getIdRoutine() {
        return idRoutine;
    }

    public void setIdRoutine(Long idRoutine) {
        this.idRoutine = idRoutine;
    }
}
