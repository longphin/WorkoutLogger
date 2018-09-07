package com.longlife.workoutlogger;

import android.app.Application;
import com.longlife.workoutlogger.data.RoomModule;

public class MyApplication
        extends Application {
    private MyApplicationComponent component;


    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerMyApplicationComponent.builder()
                .myApplicationModule(new MyApplicationModule(this))
                .roomModule(new RoomModule(this))
                .build();
    }


    public MyApplicationComponent getApplicationComponent() {
        return (component);
    }
}
