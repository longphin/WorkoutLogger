package com.longlife.workoutlogger;

import android.app.Application;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.data.RoomModule;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;
import com.longlife.workoutlogger.view.Exercises.ExercisesActivity;
import com.longlife.workoutlogger.view.Exercises.ExercisesFragment;
import com.longlife.workoutlogger.view.MainActivity;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.RoutineCreateFragment;
import com.longlife.workoutlogger.view.Routines.RoutinesActivity;
import com.longlife.workoutlogger.view.Routines.RoutinesFragment;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(
	modules = {
		AndroidInjectionModule.class,
		MyApplicationModule.class,
		RoomModule.class
	}
)
public interface MyApplicationComponent
{
	void inject(MainActivity mainActivity);
	
	void inject(RoutinesActivity roa);
	
	void inject(RoutinesFragment rof);
	
	void inject(RoutineCreateFragment rcf);
	
	void inject(ExercisesActivity eoa);
	
	void inject(ExerciseCreateFragment ecf);
	
	void inject(ExercisesFragment ef);
	
	Repository repository();
	
	Application application();
}
