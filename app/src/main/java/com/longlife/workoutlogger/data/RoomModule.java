/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/22/18 10:16 PM.
 */

package com.longlife.workoutlogger.data;

import android.content.ContentValues;
import android.util.Log;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.view.CustomViewModelFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import dagger.Module;
import dagger.Provides;

@Module
// Module for Room database.
public class RoomModule {
    // The database.
    private final Database database;
    @Inject
    public Repository repo;

    // Constructor.
    public RoomModule(MyApplication application) {
/*        // [TODO] This preloaded data is not working.
        RoomDatabase.Callback preloadedData = new RoomDatabase.Callback()
        {
            public void onCreate (SupportSQLiteDatabase db) {
                repo.insertExercise(new Exercise("dummyName", "dummyDescription"));
            }
            public void onOpen (SupportSQLiteDatabase db) {
                repo.insertExercise(new Exercise("dummyName", "dummyDescription"));
            }
        };*/

        RoomDatabase.Callback preloadedData = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                Log.d("RoomModule", "onCreate database");
                ContentValues exercise1 = new ContentValues();
                exercise1.put("name", "dummy1");

                db.insert("Exercise", OnConflictStrategy.ROLLBACK, exercise1);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };

        database = Room.databaseBuilder(
                application,
                Database.class,
                "Database.db"
        )
                //.addMigrations(MIGRATION_1_4)
                .fallbackToDestructiveMigration() //[TODO] remove this once in production. This recreates the database for every new version. Instead, we should use migrations to preserve data across different versions.
                //.addCallback(preloadedData)
                .build();

        // Run an empty transaction. This is needed to trigger the callback to create the preloaded data.
        //repo.getProfile();
        /*Executors.newSingleThreadScheduledExecutor().execute(new Runnable(){
            @Override
            public void run() {
                database.beginTransaction();
                database.endTransaction();
            }
        });*/
    }

    @Provides
    @Singleton
    Database provideDatabase() {
        return database;
    }

    @Provides
    @Singleton
    ExerciseDao provideExerciseDao(Database db) {
        return db.exerciseDao();
    }

    @Provides
    @Singleton
    RoutineDao provideRoutineDao(Database db) {
        return db.routineDao();
    }

    @Provides
    @Singleton
    ProfileDao provideProfileDao(Database db) {
        return db.profileDao();
    }

    @Provides
    @Singleton
    WorkoutDao provideWorkoutDao(Database db) {
        return db.workoutDao();
    }

    @Provides
    @Singleton
    Repository provideRepository(ExerciseDao exerciseDao, RoutineDao routineDao, ProfileDao profileDao, WorkoutDao workoutDao) {
        return new Repository(exerciseDao, routineDao, profileDao, workoutDao);
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(Repository repository) {
        return new CustomViewModelFactory(repository);
    }
}

