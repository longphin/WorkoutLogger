package com.longlife.workoutlogger;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.longlife.workoutlogger.utils.Format;
import com.longlife.workoutlogger.utils.TimeHolder;
import com.longlife.workoutlogger.view.MainActivity;

public class TimerNotificationService
        extends Service {
    public static final String BROADCAST_INTENT_NAME = "com.longlife.workoutlogger.TimerNotificationService.REQUEST_PROCESSED"; // Abitrary name, but unique within the app. Used in the intent when broadcasting result.
    public static final String EXTRA_HEADERINDEX = "headerIndex";
    // Constants - names for the values passed from the activity to this service.
    // Object that receives interaction between this service and the activity.
    private final IBinder mBinder = new LocalBinder();
    // Broadcast object to activity.
    LocalBroadcastManager broadcastManager;
    public static final String EXTRA_SETINDEX = "setIndex";
    public static final String EXTRA_MINUTES = "restMinutes";
    public static final String EXTRA_SECONDS = "restSeconds";
    private static int NOTIFICATION_ID = 49; // This can be anything, I believe.

    private CountDownTimer timer;
    private boolean timerInProgress;
    private int minutes; // Rest time minutes.
    private int seconds; // Rest time seconds.
    private int headerIndex; // Index for the header that a set is a part of. RoutineExerciseHelper.get(headerIndex)
    private int setIndex; // Index for the set within the header index. RoutineExerciseHelper.get(headerIndex).getSets().get(setIndex)

    private NotificationManagerCompat notificationManager;

    // Starts up a timer to update the notification channel.
    private void startTimer(long durationInMillis) {
        timer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeHolder timeUntilFinished = Format.getMinutesFromMillis(millisUntilFinished);
                updateNotification(createNotificationBuilder(timeUntilFinished.getMinutes(), timeUntilFinished.getSeconds()));//notificationBuilder);
            }

            @Override
            public void onFinish() {
                stopTimer();
                broadcastTimerEnded();
                stopSelf(); // Stop the service when the timer is finished.
            }
        }.start();

        timerInProgress = true;
    }

    // Creates the rest notification given the rest times.
    private NotificationCompat.Builder createNotificationBuilder(int minutes, int seconds) {
        Intent notificationIntent = new Intent(this, MainActivity.class); // [TODO] When the notification is clicked, it will bring the user to the Main Activity. May want to change this to go to the performing screen.
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        return new NotificationCompat.Builder(this, MyApplication.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Timer (" + String.valueOf(headerIndex) + " -> " + String.valueOf(setIndex) + ")")
                .setContentText(getString(R.string.notificationChannelDescription, minutes, seconds))
                .setSmallIcon(R.mipmap.temp_ic_pause_asset)//R.drawable.notification_icon_24dp) //[TODO] This temporarily uses a mipmap instead of a drawable/vector image because the android emulator errors in API 23 and below. Google should fix this later, so wait for that.
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVibrate(null)
                .setSound(null)
                .setContentIntent(pendingIntent);
    }

    // Stops timer and resets some values.
    private void stopTimer() {
        if (timer != null && timerInProgress) {
            timer.cancel();
            destroyNotification();
        }

        timerInProgress = false;
        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopTimer();

        headerIndex = intent.getIntExtra(EXTRA_HEADERINDEX, -1);
        setIndex = intent.getIntExtra(EXTRA_SETINDEX, -1);
        minutes = intent.getIntExtra(EXTRA_MINUTES, 0);
        seconds = intent.getIntExtra(EXTRA_SECONDS, 0);

        startForeground(NOTIFICATION_ID, createNotificationBuilder(minutes, seconds).build());

        startTimer(Format.convertToMilliseconds(minutes, seconds));

        return START_NOT_STICKY; // [TODO] Need to learn about the different types and make sure what happens when the system destroys our notification.
    }

    public void broadcastTimerEnded() {
        Intent intent = new Intent(BROADCAST_INTENT_NAME);
        intent.putExtra(EXTRA_HEADERINDEX, headerIndex);
        intent.putExtra(EXTRA_SETINDEX, setIndex);
        broadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        broadcastManager = LocalBroadcastManager.getInstance(this);
        notificationManager = NotificationManagerCompat.from(this);
    }

    // Destroy notification.
    private void destroyNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
        notificationManager.cancelAll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void updateNotification(NotificationCompat.Builder builder) {
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public class LocalBinder extends Binder {
        public TimerNotificationService getService() {
            return TimerNotificationService.this;
        }
    }
}
