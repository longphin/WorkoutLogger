package com.longlife.workoutlogger.v2.data;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.v2.view.CustomViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    private final Database db;

    public RoomModule(MyApplication application) {
        db = Room.databaseBuilder(
                application,
                Database.class,
                "Database")
                .build();
    }

    @Provides
    @Singleton
    Database provideDatabase() {
        return (db);
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
    ViewModelProvider.Factory provideViewModelFactory(Repository repository) {
        return new CustomViewModelFactory(repository);
    }
}
