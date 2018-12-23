/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/22/18 10:16 PM.
 */

package com.longlife.workoutlogger.model.PreLoadedData;

import com.longlife.workoutlogger.model.Exercise.Exercise;

import androidx.room.PrimaryKey;

public class PreLoadedExercise extends Exercise {
    @PrimaryKey
    private Long idPreloadedExercise;
    private boolean requiresUpdate = false;
}
