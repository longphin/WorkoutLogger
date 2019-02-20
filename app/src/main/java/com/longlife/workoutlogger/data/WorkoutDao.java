/*
 * Created by Longphi Nguyen on 2/3/19 11:30 AM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/3/19 11:30 AM.
 */

package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.model.Workout.WorkoutProgram;
import com.longlife.workoutlogger.model.Workout.WorkoutProgramShort;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract Long createWorkoutProgram(WorkoutProgram program);

    @Query("SELECT wp.*" +
            " FROM WorkoutProgram as wp" +
            " WHERE isSaved=0 AND 0=1" + // [TODO] remove the 0=1 once we have the process where a workout is restored/edited functionality in place.
            " LIMIT 1")
    public abstract Maybe<WorkoutProgram> getFirstNonSavedWorkout();

    @Query("SELECT wp.idWorkout, wp.name, group_concat(coalesce(r.name,' Unnamed'), ', ') as routineConcatenated" +
            " FROM WorkoutProgram as wp" +
            " LEFT JOIN WorkoutRoutine as wr on wp.idWorkout=wr.idWorkout" +
            " LEFT JOIN Routine as r on wr.idRoutine=r.idRoutine" +
            " GROUP BY wp.idWorkout")
    public abstract Single<List<WorkoutProgramShort>> getWorkoutShortList();
}
