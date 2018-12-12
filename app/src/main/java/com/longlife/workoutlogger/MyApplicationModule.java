/*
 * Created by Longphi Nguyen on 12/11/18 8:26 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

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
