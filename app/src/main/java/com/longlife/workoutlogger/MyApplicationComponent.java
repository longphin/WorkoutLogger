package com.longlife.workoutlogger;

import com.longlife.workoutlogger.v2.dependencyinjection.DatabaseModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(
        modules = {
                AndroidInjectionModule.class,
                MyApplicationModule.class,
                DatabaseModule.class
        }
)
public interface MyApplicationComponent extends AndroidInjector<MyApplication> {
}
