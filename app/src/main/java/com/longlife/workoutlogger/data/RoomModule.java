package com.longlife.workoutlogger.data;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.view.CustomViewModelFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
// Module for Room database.
public class RoomModule {
    // The database.
    private final Database database;

    // Constructor.
    public RoomModule(MyApplication application) {
        database = Room.databaseBuilder(
                application,
                Database.class,
                "Database.db"
        )
                //.addMigrations(MIGRATION_1_4)
                .fallbackToDestructiveMigration() //[TODO] remove this once in production. This recreates the database for every new version. Instead, we should use migrations to preserve data across different versions.
                .build();
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
    Repository provideRepository(ExerciseDao exerciseDao, RoutineDao routineDao, ProfileDao profileDao) {
        return new Repository(exerciseDao, routineDao, profileDao);
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(Repository repository) {
        return new CustomViewModelFactory(repository);
    }
}

