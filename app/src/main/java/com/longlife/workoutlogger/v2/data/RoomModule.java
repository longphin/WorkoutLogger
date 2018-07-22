package com.longlife.workoutlogger.v2.data;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

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
public class RoomModule
{
	// Private
	private final Database database;
	private CompositeDisposable disposables = new CompositeDisposable();
	
	public RoomModule(MyApplication application)
	{
		RoomDatabase.Callback populateData = new RoomDatabase.Callback()
		{
			// Overrides
			@Override
			public void onCreate(@android.support.annotation.NonNull SupportSQLiteDatabase db)
			{
				super.onCreate(db);
				
				// [TODO] This is supposed to pre-populate the database with routines and exercises, but it is not working.
				Observable initialRoutines = Observable.fromCallable(
					() -> database.routineDao().insertRoutine(new Routine()))
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread());
				
				Observable initialExercises = Observable.fromCallable(
					() -> database.exerciseDao().insertExercise(new Exercise()))
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread());
				
				DisposableObserver oInitialRoutines = new DisposableObserver<Long>()
				{
					// Overrides
					@Override
					public void onNext(@NonNull Long l)
					{
					}
					
					@Override
					public void onError(@NonNull Throwable e)
					{
					}
					
					@Override
					public void onComplete()
					{
					}
				};
				
				DisposableObserver oInitialExercises = new DisposableObserver<Long>()
				{
					// Overrides
					@Override
					public void onNext(Long aLong)
					{
					
					}
					
					@Override
					public void onError(Throwable e)
					{
					
					}
					
					@Override
					public void onComplete()
					{
					
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
			public void onOpen(@android.support.annotation.NonNull SupportSQLiteDatabase db)
			{
				super.onOpen(db);
			}
		};
		
		database = Room.databaseBuilder(
			application,
			Database.class,
			"Database.db"
		)
			//.addCallback(populateData)
			//.addMigrations(MIGRATION_1_4)
			.fallbackToDestructiveMigration()
			.build();
	}
	
	@Provides
	@Singleton
	Database provideDatabase()
	{
		return database;
	}
	
	@Provides
	@Singleton
	ExerciseDao provideExerciseDao(Database db)
	{
		return db.exerciseDao();
	}
	
	@Provides
	@Singleton
	RoutineDao provideRoutineDao(Database db)
	{
		return db.routineDao();
	}
	
	@Provides
	@Singleton
	Repository provideRepository(ExerciseDao exerciseDao, RoutineDao routineDao)
	{
		return new Repository(exerciseDao, routineDao);
	}
	
	@Provides
	@Singleton
	ViewModelProvider.Factory provideViewModelFactory(Repository repository)
	{
		return new CustomViewModelFactory(repository);
	}
}
// Inner Classes
