package com.longlife.workoutlogger.data;

import android.arch.persistence.room.RoomDatabase;
import com.longlife.workoutlogger.model.*;

/**
 * Created by Longphi on 1/5/2018.
 */

@android.arch.persistence.room.Database(
        entities =
                {
                        Profile.class,
                        Exercise.class,
                        ExerciseHistory.class,
                        Routine.class,
                        RoutineHistory.class,
                        RoutineSession.class,
                        SessionExercise.class,
                        SessionExerciseSet.class
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


