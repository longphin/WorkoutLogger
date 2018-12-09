package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class MuscleEntity {
    @PrimaryKey
    private Integer idMuscle;
    @NonNull
    private Integer idMuscleGroup;

    public MuscleEntity() {
    }

    public MuscleEntity(Integer idMuscle, @NonNull Integer idMuscleGroup) {
        this.idMuscle = idMuscle;
        this.idMuscleGroup = idMuscleGroup;
    }

    public Integer getIdMuscle() {
        return idMuscle;
    }

    public void setIdMuscle(Integer idMuscle) {
        this.idMuscle = idMuscle;
    }

    @NonNull
    public Integer getIdMuscleGroup() {
        return idMuscleGroup;
    }

    public void setIdMuscleGroup(@NonNull Integer idMuscleGroup) {
        this.idMuscleGroup = idMuscleGroup;
    }
}
