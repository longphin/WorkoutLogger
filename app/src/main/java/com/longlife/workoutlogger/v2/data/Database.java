package com.longlife.workoutlogger.v2.data;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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
        version = 1
)
public abstract class Database extends RoomDatabase {
    private static Database INSTANCE;

    public abstract Dao dao();

    public Database() {

    }

    public static Database getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "database")
                    .build();
        }
        return (INSTANCE);
    }


}
