/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/9/18 11:05 AM.
 */

package com.longlife.workoutlogger.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MuscleGroupEntity {
    @PrimaryKey
    private Integer idMuscleGroup;

    public MuscleGroupEntity(Integer idMuscleGroup) {

        this.idMuscleGroup = idMuscleGroup;
    }

    public Integer getIdMuscleGroup() {
        return idMuscleGroup;
    }

    public void setIdMuscleGroup(Integer idMuscleGroup) {
        this.idMuscleGroup = idMuscleGroup;
    }
}
