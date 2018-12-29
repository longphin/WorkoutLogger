/*
 * Created by Longphi Nguyen on 12/11/18 8:26 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/4/18 6:41 PM.
 */

package com.longlife.workoutlogger;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.longlife.workoutlogger.data.RoomModule;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.utils.JSONParser;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;

import androidx.core.content.ContextCompat;

/*
[TODO] Change how exercises are selected when creating a routine. Instead of opening another fragment, just have two recyclerviews side by side. Left side is for exercises in routine, right side is for entire list of exercises. Items from the right can be clicked on or dragged to the left side.

[TODO] Allow users to create Workouts, which schedule routines by day or every "x" days.
    [TODO] Allow users to activate/deactivate Workouts.
    {TODO] Create planner and calendar to see upcoming routines. The listed routines will be based on the active Workouts.

[TODO] Allow rest timer to be stopped, reset, continued.
[TODO] Allow user to set target goals for each exercise.
[TODO] optimize recyclerviews
[TODO] optimize images once thumbnails/images are added to the exercise. https://medium.freecodecamp.org/how-we-reduced-memory-footprint-by-50-in-our-android-app-49efa5c93ad8

[TODO] Add user preference settings. - PreferenceFragments etc.
[TODO] Create variations for exercises that users can swap in and out.
[TODO] Put cached objects in repository.
[TODO Premium] Allow users to upload progress pictures of themselves. Have them time-stamped so they can also be used to verify challenges. -- Premium feature.
[TODO Premium] Allow users to upload their own image for an exercise. -- Premium feature

[TODO] Link exercises to create a superset. This will alternate sets between the exercises.

[TODO] Preload dummy data. May want to see if Room eventually provides a better way to do this.
    https://medium.com/@johann.pardanaud/ship-an-android-app-with-a-pre-populated-database-cd2b3aa3311f
    https://stackoverflow.com/questions/513084/ship-an-application-with-a-database

[TODO Premium] Add server and API.
    [TODO] Add user login and verification. Identify if user is premium.
    [TODO] Add syncing to cloud/server. -- Premium feature
        - See developer.android.com/jetpack/docs/guide for architecture tutorial for syncing database with webservice data.
    [TODO] Add occasional "challenges" Workouts. Eventually, there should be multiple challenges available, related user's goal. But initially, these will probably be the same challenges for everyone for the duration.
            For example, everyone can opt-in to the month's weight-loss challenge.

// LOW PRIORITY
[TODO] Make recyclerview header "stick" to the top when scrolling (optional, for routines / performing) stackoverflow.com/questions/32949971
[TODO] Pushing an image upward when scrolling (optional, low priority) stackoverflow.com/questions/28027289
*/
public class MyApplication
        extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    private MyApplicationComponent component;
    public static final String NOTIFICATION_CHANNEL_NAME = "Rest Timer";
    public static String NOTIFICATION_CHANNEL_ID = "RestTimerNotification"; // This can be anything, I believe.
    private static String NOTIFICATION_CHANNEL_DESCRIPTION = "Displays time remaining while resting.";
    // Service for the rest timer notification.
    private TimerNotificationService timerNotificationService;
    private boolean timerNotificationBound = false;
    private ServiceConnection timerNotificationConnection = new ServiceConnection() {
        // A connection with the service has been established. This service will run in the same process as this activity.
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TimerNotificationService.LocalBinder binder = (TimerNotificationService.LocalBinder) iBinder;
            timerNotificationService = binder.getService();
            timerNotificationBound = true;
            Log.d(TAG, "Timer service bounded.");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            timerNotificationBound = false;
            Log.d(TAG, "Timer service unbounded.");
            // [TODO] Why is the service not being disconnected? Should this trigger when the service uses stopSelf()?
        }
    };

    private static final String PRELOAD_FILE_1 = "preloadedFile1.json";

    @Override
    public void onCreate() {
        super.onCreate();
        // install Canary to help detect memory leaks.
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        component = DaggerMyApplicationComponent.builder()
                .myApplicationModule(new MyApplicationModule(this))
                .roomModule(new RoomModule(this))
                .build();

        createNotificationChannel();

        //preloadData();
    }

    private void preloadData() {
        File file1 = new File(PRELOAD_FILE_1);
        if (!file1.exists()) {
            JSONParser.writeToFile(this, PRELOAD_FILE_1, (new Exercise("dummy1", "note1")).toJSON()); // [TODO] need to write an array of exercises. Can use GSON for this?
        }

        // [TODO] create a service to regularly insert preloaded data. When an exercise is inserted, then remember to change exercise.isPreloaded = true and insert the "isPreloaded":"true" in the dummy.json for the exercise.
        //List<Exercise> preloadedExercises = JSONParser.getExercisesToPreload(this, "dummy.json");
        String res = JSONParser.readFromFile(this, PRELOAD_FILE_1);
        //Exercise ex = gson.fromJson(res, Exercise.class);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // Unbind from timer service.
        if (timerNotificationBound) {
            unbindService(timerNotificationConnection);
            timerNotificationBound = false;
        }
    }

    public void startTimerNotificationService(View v, int headerIndex, int setIndex, int minutes, int seconds) {
        Intent serviceIntent = new Intent(this, TimerNotificationService.class);
        serviceIntent.putExtra(TimerNotificationService.EXTRA_HEADERINDEX, headerIndex);
        serviceIntent.putExtra(TimerNotificationService.EXTRA_SETINDEX, setIndex);
        serviceIntent.putExtra(TimerNotificationService.EXTRA_MINUTES, minutes);
        serviceIntent.putExtra(TimerNotificationService.EXTRA_SECONDS, seconds);

        ContextCompat.startForegroundService(this, serviceIntent);
        bindService(serviceIntent, timerNotificationConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopTimerNotificationService() {
        Intent serviceIntent = new Intent(this, TimerNotificationService.class);
        stopService(serviceIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            notificationChannel.enableVibration(false);
            notificationChannel.setSound(null, null);

            NotificationManager manager = getSystemService(NotificationManager.class);
            //manager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    public MyApplicationComponent getApplicationComponent() {
        return (component);
    }
}
