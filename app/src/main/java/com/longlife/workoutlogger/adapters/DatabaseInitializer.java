package com.longlife.workoutlogger.adapters;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.longlife.workoutlogger.model.Routine;

/**
 * Created by Longphi on 4/15/2018.
 */

public class DatabaseInitializer {
    public static void populateAsync(@NonNull final Database db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final Database db) {
        populateWithTestData(db);
    }

    private static void addRoutine(final Database db, Routine routine) {
        db.dao().addRoutines(routine);
    }

    private static void populateWithTestData(Database db) {
        for (int i = 0; i < 9; i++) {
            Routine routineToAdd = new Routine("", "", true);
            routineToAdd.setName("routine " + String.valueOf(routineToAdd.getIdRoutine()) + " name");
            routineToAdd.setDescription("routine " + String.valueOf(routineToAdd.getIdRoutine()) + " description");
            //routines.put(routineToAdd.getIdRoutine(), routineToAdd);
            addRoutine(db, routineToAdd);
        }
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final Database mDb;

        PopulateDbAsync(Database db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return (null);
        }

    }
}
