package com.longlife.workoutlogger;

import android.app.Application;

import com.longlife.workoutlogger.v2.data.Dao;
import com.longlife.workoutlogger.v2.data.Database;
import com.longlife.workoutlogger.v2.data.Repository;
import com.longlife.workoutlogger.v2.data.RoomModule;
import com.longlife.workoutlogger.v2.view.RoutineOverview.RoutinesOverviewFragment;
import com.longlife.workoutlogger.view.MainActivity;

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

    void inject(RoutinesOverviewFragment rof);
    //void inject(MyApplication application);

    Dao dao();

    Database db();

    Repository repository();

    Application application();
}
