/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/9/18 10:26 AM.
 */

package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.ExerciseMuscle;
import com.longlife.workoutlogger.model.MuscleEntity;
import com.longlife.workoutlogger.model.MuscleGroupEntity;
import com.longlife.workoutlogger.model.Profile;
import com.longlife.workoutlogger.model.Routine.Routine;
import com.longlife.workoutlogger.model.Routine.RoutineExercise;
import com.longlife.workoutlogger.model.Routine.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.model.Workout.WorkoutProgram;
import com.longlife.workoutlogger.model.Workout.WorkoutRoutine;

import androidx.room.RoomDatabase;

/**
 * Created by Longphi on 1/5/2018.
 */

@androidx.room.Database(
        entities =
                {
                        Profile.class,
                        Exercise.class,
                        Routine.class,
                        RoutineSession.class,
                        SessionExercise.class,
                        SessionExerciseSet.class,
                        ExerciseMuscle.class,
                        WorkoutProgram.class,
                        MuscleEntity.class,
                        MuscleGroupEntity.class,
                        RoutineExercise.class,
                        WorkoutRoutine.class
                },
        version = DatabaseVersion.CURRENT
)
public abstract class Database
        extends RoomDatabase {
    public static final int VERSION = DatabaseVersion.CURRENT;

    public abstract ExerciseDao exerciseDao();

    public abstract RoutineDao routineDao();

    public abstract ProfileDao profileDao();

    public abstract WorkoutDao workoutDao();
}


