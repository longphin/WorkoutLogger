package com.longlife.workoutlogger;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class MyApplicationModule {
    private final MyApplication application;

    //@Inject
    //public Repository repo;

    public MyApplicationModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    MyApplication provideMyApplication() {
        return (application);
    }

    @Provides
    Application provideApplication() {
        return (application);
    }
}
