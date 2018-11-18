package com.longlife.workoutlogger.data;

import android.arch.persistence.room.RoomDatabase;

import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.ExerciseMuscle;
import com.longlife.workoutlogger.model.Profile;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

/**
 * Created by Longphi on 1/5/2018.
 */

@android.arch.persistence.room.Database(
        entities =
                {
                        Profile.class,
                        Exercise.class,
                        Routine.class,
                        RoutineSession.class,
                        SessionExercise.class,
                        SessionExerciseSet.class,
                        ExerciseMuscle.class
                },
        version = DatabaseVersion.CURRENT
)
public abstract class Database
        extends RoomDatabase {
    public static final int VERSION = DatabaseVersion.CURRENT;

    public abstract ExerciseDao exerciseDao();

    public abstract RoutineDao routineDao();

    public abstract ProfileDao profileDao();
}


