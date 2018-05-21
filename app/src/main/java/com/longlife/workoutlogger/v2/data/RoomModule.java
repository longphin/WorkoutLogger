package com.longlife.workoutlogger.v2.data;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.v2.view.CustomViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    private final Database db;

    public RoomModule(MyApplication application) {
        RoomDatabase.Callback populateData = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@android.support.annotation.NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues content = new ContentValues();
                content.put("name", "name1");
                content.put("description", "descrip1");
                db.insert("routine", OnConflictStrategy.REPLACE, content);
            }

            @Override
            public void onOpen(@android.support.annotation.NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };

        db = Room.databaseBuilder(
                application,
                Database.class,
                "Database.db")
                .addCallback(populateData)
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
