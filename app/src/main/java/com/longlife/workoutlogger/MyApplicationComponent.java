package com.longlife.workoutlogger;

import com.longlife.workoutlogger.v2.dependencyinjection.RoomModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(
        modules = {
                AndroidInjectionModule.class,
                MyApplicationModule.class,
                RoomModule.class
        }
)
public interface MyApplicationComponent extends AndroidInjector<MyApplication> {
}
