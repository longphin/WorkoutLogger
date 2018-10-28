package com.longlife.workoutlogger;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.longlife.workoutlogger.data.RoomModule;

/*
[TODO] Allow changing weight metric when editing a set.
[TODO] Allow different type of exercises.
[TODO] Change exercise lists to use a ShortExercise object instead. We do not need the entire exercise details, just the id, name, type, etc.
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
[TODO] Allow users to upload progress pictures of themselves. Have them time-stamped so they can also be used to verify challenges.
[TODO] Allow users to upload their own image for an exercise.

[TODO] Add server and API.
    [TODO] Add syncing to cloud/server.
    [TODO] Add occasional "challenges" Workouts. Eventually, there should be multiple challenges available, related user's goal. But initially, these will probably be the same challenges for everyone for the duration.
            For example, everyone can opt-in to the month's weight-loss challenge.

[TODO] Link exercises to create a superset. This will alternate sets between the exercises.
[TODO] Pushing an image upward when scrolling (optional, low priority) stackoverflow.com/questions/28027289
*/
public class MyApplication
        extends Application {
    private MyApplicationComponent component;
    private static final String NOTIFICATION_CHANNEL_NAME = "Rest Timer Notification";
    public static String NOTIFICATION_CHANNEL_ID = "RestTimerNotification"; // This can be anything, I believe.
    private static String NOTIFICATION_CHANNEL_DESCRIPTION = "Displays time remaining while resting.";

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerMyApplicationComponent.builder()
                .myApplicationModule(new MyApplicationModule(this))
                .roomModule(new RoomModule(this))
                .build();

        createNotificationChannel();
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
