/*
 * Created by Longphi Nguyen on 12/11/18 8:26 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/6/18 8:09 AM.
 */

package com.longlife.workoutlogger;

import android.app.Application;

import com.longlife.workoutlogger.data.Repository;
import com.longlife.workoutlogger.data.RoomModule;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;
import com.longlife.workoutlogger.view.Exercises.EditExercise.ExerciseEditFragment;
import com.longlife.workoutlogger.view.Exercises.ExercisesFragment;
import com.longlife.workoutlogger.view.Exercises.ExercisesListFragment;
import com.longlife.workoutlogger.view.InitializeActivity;
import com.longlife.workoutlogger.view.MainActivity;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine.ExercisesSelectableFragment;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.RoutineCreateFragment;
import com.longlife.workoutlogger.view.Routines.RoutinesFragment;
import com.longlife.workoutlogger.view.Workout.Create.EditRoutineDetailsDialog;
import com.longlife.workoutlogger.view.Workout.Create.EditRoutineExerciseDialog;
import com.longlife.workoutlogger.view.Workout.Create.RoutineFragment;
import com.longlife.workoutlogger.view.Workout.Create.WorkoutCreateFragment;
import com.longlife.workoutlogger.view.Workout.WorkoutListFragment;

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
public interface MyApplicationComponent {
    void inject(InitializeActivity a);

    void inject(MainActivity a);

    void inject(RoutinesFragment rof);

    void inject(RoutineCreateFragment rcf);

    void inject(ExerciseCreateFragment efdfb);

    void inject(ExercisesFragment ef);

    void inject(ExercisesSelectableFragment esf);

    void inject(ExerciseEditFragment eef);

    void inject(ExercisesListFragment elf);

    void inject(WorkoutCreateFragment wcf);

    void inject(RoutineFragment rf);

    void inject(WorkoutListFragment wlf);

    void inject(EditRoutineDetailsDialog erdd);

    void inject(EditRoutineExerciseDialog ered);

    Repository repository();

    Application application();
}
