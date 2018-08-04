package com.longlife.workoutlogger.data;

import android.arch.persistence.room.RoomDatabase;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseHistory;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineHistory;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

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
			SessionExerciseSet.class,
			RoutineHistory.class,
			ExerciseHistory.class
		},
	version = DatabaseVersion.CURRENT
)
public abstract class Database
	extends RoomDatabase
{
	public static final int VERSION = DatabaseVersion.CURRENT;
	
	public abstract ExerciseDao exerciseDao();
	
	public abstract RoutineDao routineDao();

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

// Inner Classes
