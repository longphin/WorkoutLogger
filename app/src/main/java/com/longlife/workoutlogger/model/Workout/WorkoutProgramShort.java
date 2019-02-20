/*
 * Created by Longphi Nguyen on 2/18/19 2:37 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/18/19 2:37 PM.
 */

package com.longlife.workoutlogger.model.Workout;

public class WorkoutProgramShort {
    private Long idWorkout;
    private String name;
    private String routineConcatenated;

    public WorkoutProgramShort(Long idWorkout, String name, String routineConcatenated) {
        this.idWorkout = idWorkout;
        this.name = name;
        this.routineConcatenated = routineConcatenated;
    }

    public Long getIdWorkout() {
        return idWorkout;
    }

    public void setIdWorkout(Long idWorkout) {
        this.idWorkout = idWorkout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoutineConcatenated() {
        return routineConcatenated;
    }

    public void setRoutineConcatenated(String routineConcatenated) {
        this.routineConcatenated = routineConcatenated;
    }
}
