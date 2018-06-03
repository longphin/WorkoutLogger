package com.longlife.workoutlogger.v2.data;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.Routine;
import com.longlife.workoutlogger.v2.view.CustomViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@Module
public class RoomModule {
    private final Database database;
    private CompositeDisposable disposables = new CompositeDisposable();

    private final Migration MIGRATION_1_4 = new Migration(1, 4) //[TODO] this migration doesn't populate database either. Delete this.
    {
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            Observable initialRoutines = Observable.fromCallable(
                    () -> database.dao().insertRoutine(new Routine()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            Observable initialExercises = Observable.fromCallable(
                    () -> database.dao().insertExercise(new Exercise()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            DisposableObserver oInitialRoutines = new DisposableObserver<Long>() {
                @Override
                public void onNext(@NonNull Long l) {
                }

                @Override
                public void onError(@NonNull Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            };

            DisposableObserver oInitialExercises = new DisposableObserver<Long>() {
                @Override
                public void onNext(Long aLong) {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            };

            initialRoutines.subscribeWith(oInitialRoutines);
            disposables.add(oInitialRoutines);
            initialExercises.subscribeWith(oInitialExercises);
            disposables.add(oInitialExercises);
        }
    };

    public RoomModule(MyApplication application) {
        RoomDatabase.Callback populateData = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@android.support.annotation.NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                // [TODO] This is supposed to pre-populate the database with routines and exercises, but it is not working.
                Observable initialRoutines = Observable.fromCallable(
                        () -> database.dao().insertRoutine(new Routine()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                Observable initialExercises = Observable.fromCallable(
                        () -> database.dao().insertExercise(new Exercise()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                DisposableObserver oInitialRoutines = new DisposableObserver<Long>() {
                    @Override
                    public void onNext(@NonNull Long l) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                };

                DisposableObserver oInitialExercises = new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };

                initialRoutines.subscribeWith(oInitialRoutines);
                disposables.add(oInitialRoutines);
                initialExercises.subscribeWith(oInitialExercises);
                disposables.add(oInitialExercises);
                // [TODO] need to dispose of these observables.

                /*
                // Insert routines.
                ContentValues routineContent = new ContentValues();
                routineContent.put("name", "name1");
                routineContent.put("description", "descrip1");
                db.insert("routine", OnConflictStrategy.REPLACE, routineContent);

                // Insert exercises.
                ContentValues exerciseContent = new ContentValues();
                exerciseContent.put("name", "name1");
                exerciseContent.put("description", "descrip1");
                db.insert("exercises", OnConflictStrategy.REPLACE, exerciseContent);
                */
            }

            @Override
            public void onOpen(@android.support.annotation.NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };

        database = Room.databaseBuilder(
                application,
                Database.class,
                "Database.db")
                .addCallback(populateData)
                .addMigrations(MIGRATION_1_4)
                .build();
    }

    @Provides
    @Singleton
    Database provideDatabase() {
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
    ViewModelProvider.Factory provideViewModelFactory(Repository repository) {
        return new CustomViewModelFactory(repository);
    }
}
