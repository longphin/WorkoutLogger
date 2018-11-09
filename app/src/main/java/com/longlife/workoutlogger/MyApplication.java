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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.longlife.workoutlogger.data.RoomModule;

/*
[TODO] Allow different type of exercises.
[TODO] Put cached objects in repository.
[TODO] Separate groups in the recycler views better. stackoverflow.com/questions/31273203
[TODO] Make recyclerview header "stick" to the top when scrolling (optional, for routines / performing) stackoverflow.com/questions/32949971
[TODO] Allow search for exercises by name, category, muscle groups.
[TODO] Allow rest timer to be stopped, reset, continued.
[TODO] Allow user to set target goals for each exercise.
[TODO] Allow users to create Workouts, which schedule routines by day or every "x" days.
[TODO] Allow users to activate/deactivate Workouts.
{TODO] Create planner and calendar to see upcoming routines. The listed routines will be based on the active Workouts.
[TODO] Create variations for exercises that users can swap in and out.
[TODO] Allow users to upload progress pictures of themselves. Have them time-stamped so they can also be used to verify challenges. -- Premium feature.
[TODO] Allow users to upload their own image for an exercise. -- Premium feature
[TODO] Add user preference settings. - PreferenceFragments etc.

[TODO] Add server and API.
    [TODO] Add user login and verification. Identify if user is premium.
    [TODO] Add syncing to cloud/server. -- Premium feature
    [TODO] Add occasional "challenges" Workouts. Eventually, there should be multiple challenges available, related user's goal. But initially, these will probably be the same challenges for everyone for the duration.
            For example, everyone can opt-in to the month's weight-loss challenge.

[TODO] Link exercises to create a superset. This will alternate sets between the exercises.
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

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerMyApplicationComponent.builder()
                .myApplicationModule(new MyApplicationModule(this))
                .roomModule(new RoomModule(this))
                .build();

        createNotificationChannel();
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
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    public MyApplicationComponent getApplicationComponent() {
        return (component);
    }
}
