package com.longlife.workoutlogger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

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

    @Singleton
    @Provides
    Context provideContext(MyApplication application) {
        return (application);
    }
}
