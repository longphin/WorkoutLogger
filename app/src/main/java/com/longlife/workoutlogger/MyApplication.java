package com.longlife.workoutlogger;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.NonNull;

import com.longlife.workoutlogger.data.RoomModule;

public class MyApplication
        extends Application {
    private MyApplicationComponent component;
    public static final String NOTIFICATION_CHANNEL_NAME = "notificationChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerMyApplicationComponent.builder()
                .myApplicationModule(new MyApplicationModule(this))
                .roomModule(new RoomModule(this))
                .build();

        createNotificationChannel(NOTIFICATION_CHANNEL_NAME);
    }

    private void createNotificationChannel(@NonNull String IdNotificationChannel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(IdNotificationChannel, "example channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }


    }


    public MyApplicationComponent getApplicationComponent() {
        return (component);
    }
}
