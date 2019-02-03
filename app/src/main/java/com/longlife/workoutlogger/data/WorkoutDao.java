/*
 * Created by Longphi Nguyen on 2/3/19 11:30 AM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/3/19 11:30 AM.
 */

package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.model.WorkoutProgram;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import io.reactivex.Single;

@Dao
public abstract class WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract Single<Long> createWorkoutProgram(WorkoutProgram program);
}
