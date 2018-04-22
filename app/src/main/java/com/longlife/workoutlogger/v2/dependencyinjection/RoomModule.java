package com.longlife.workoutlogger.v2.dependencyinjection;

import android.arch.persistence.room.Room;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.v2.data.Dao;
import com.longlife.workoutlogger.v2.data.Database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    private final Database database;

    public RoomModule(MyApplication application) {
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

    /*
    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(Database db)
    {
        return(new CustomViewModelFactory(db));
    }
    */
}
