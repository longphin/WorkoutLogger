package com.longlife.workoutlogger.v2.data;

import android.arch.persistence.room.RoomDatabase;

import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.Routine;
import com.longlife.workoutlogger.v2.model.RoutineSession;
import com.longlife.workoutlogger.v2.model.SessionExercise;
import com.longlife.workoutlogger.v2.model.SessionExerciseSet;

/**
 * Created by Longphi on 1/5/2018.
 */

@android.arch.persistence.room.Database(
        entities =
                {
                        Exercise.class,
                        Routine.class,
                        RoutineSession.class,
                        SessionExercise.class,
                        SessionExerciseSet.class
                },
        version = Database.VERSION
)
public abstract class Database extends RoomDatabase {
    public static final int VERSION = 1; // Initial version

    public abstract Dao dao();

    /*
    private static Database INSTANCE;

    public static Database getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "database")
                    .build();
        }
        return (INSTANCE);
    }
    */
}
