/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/9/18 11:05 AM.
 */

package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class MuscleEntity {
    @PrimaryKey
    private Integer idMuscle;
    private Integer idMuscleGroup;

    public MuscleEntity() {
    }

    public MuscleEntity(Integer idMuscle, Integer idMuscleGroup) {
        this.idMuscle = idMuscle;
        this.idMuscleGroup = idMuscleGroup;
    }

    public Integer getIdMuscle() {
        return idMuscle;
    }

    public void setIdMuscle(Integer idMuscle) {
        this.idMuscle = idMuscle;
    }

    public Integer getIdMuscleGroup() {
        return idMuscleGroup;
    }

    public void setIdMuscleGroup(Integer idMuscleGroup) {
        this.idMuscleGroup = idMuscleGroup;
    }
}
