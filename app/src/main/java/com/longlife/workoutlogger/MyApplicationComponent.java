package com.longlife.workoutlogger;

import android.app.Application;

import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.data.RoomModule;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExerciseCreateFragment;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesOverviewActivity;
import com.longlife.workoutlogger.v2.view.ExercisesOverview.ExercisesOverviewFragment;
import com.longlife.workoutlogger.v2.view.MainActivity;
import com.longlife.workoutlogger.v2.view.RoutineOverview.RoutineCreateFragment;
import com.longlife.workoutlogger.v2.view.RoutineOverview.RoutinesOverviewActivity;
import com.longlife.workoutlogger.v2.view.RoutineOverview.RoutinesOverviewFragment;

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
    void inject(MainActivity mainActivity);

    void inject(RoutinesOverviewActivity roa);
    void inject(RoutinesOverviewFragment rof);

    void inject(RoutineCreateFragment rcf);

    void inject(ExercisesOverviewActivity eoa);
    void inject(ExercisesOverviewFragment eof);

    void inject(ExerciseCreateFragment ecf);

    Repository repository();

    Application application();
}
