/*
 * Created by Longphi Nguyen on 2/18/19 2:36 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/3/19 12:35 PM.
 */

package com.longlife.workoutlogger.model.Routine;

import com.longlife.workoutlogger.model.Exercise.Exercise;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Routine.class, parentColumns = "idRoutine", childColumns = "idRoutine"),
        @ForeignKey(entity = Exercise.class, parentColumns = "idExercise", childColumns = "idExercise")
},
        indices = {
                @Index(value = {"idRoutine"}),
                @Index(value = {"idExercise"})
        }
)
public class RoutineExercise {
    @PrimaryKey
    private Long idRoutineExercise;
    private Long idRoutine;
    private Long idExercise;

    private int numberOfSets;

    public RoutineExercise(Long idRoutineExercise, Long idRoutine, Long idExercise, int numberOfSets) {
        this.idRoutineExercise = idRoutineExercise;
        this.idRoutine = idRoutine;
        this.idExercise = idExercise;
        this.numberOfSets = numberOfSets;
    }

    @Ignore
    public RoutineExercise(Long idRoutine, Long idExercise, int numberOfSets) {
        this.idRoutine = idRoutine;
        this.idExercise = idExercise;
        this.numberOfSets = numberOfSets;
    }

    public int getNumberOfSets() {
        return numberOfSets;
    }

    public void setNumberOfSets(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

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
