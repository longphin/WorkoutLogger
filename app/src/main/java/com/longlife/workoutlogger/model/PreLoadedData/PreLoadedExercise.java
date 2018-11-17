package com.longlife.workoutlogger.model.PreLoadedData;

import android.arch.persistence.room.PrimaryKey;

import com.longlife.workoutlogger.model.Exercise.Exercise;

public class PreLoadedExercise extends Exercise {
    @PrimaryKey
    private Long idPreloadedExercise;
    private boolean requiresUpdate = false;
}
