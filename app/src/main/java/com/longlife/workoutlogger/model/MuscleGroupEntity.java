package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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
