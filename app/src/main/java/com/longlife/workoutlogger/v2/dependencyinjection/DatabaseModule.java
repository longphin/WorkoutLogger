package com.longlife.workoutlogger.v2.dependencyinjection;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.v2.data.Dao;
import com.longlife.workoutlogger.v2.data.Database;
import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.view.CustomViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    private final Database database;

    public DatabaseModule(MyApplication application) {
        this.database =
                Room.databaseBuilder(
                        application,
                        Database.class,
                        "Database"
                ).build();
    }

    @Provides
    @Singleton
    Database provideDatabase(MyApplication application) {
        return (database);
    }

    @Provides
    @Singleton
    Dao provideDao(Database db) {
        return (db.dao());
    }

    @Provides
    @Singleton
    Repository provideRepository(Dao dao) {
        return (new Repository(dao));
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(Repository repo)
    {
        return (new CustomViewModelFactory(repo));
    }
}
