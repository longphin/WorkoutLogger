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

import com.longlife.workoutlogger.utils.Format;
import com.longlife.workoutlogger.utils.TimeHolder;
import com.longlife.workoutlogger.view.MainActivity;

public class TimerNotificationService
        extends Service {
    // Constants - names for the values passed from the activity to this service.
    public static final String EXTRA_HEADERINDEX = "headerIndex";
    // Object that receives interaction between this service and the activity.
    private final IBinder mBinder = new LocalBinder();
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
                //updateNotification(createNotificationBuilder(0,0));//notificationBuilder);
                stopTimer();
                stopSelf(); // Stop the service when the timer is finished.
            }
        }.start();

        timerInProgress = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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

    // Creates the rest notification given the rest times.
    private NotificationCompat.Builder createNotificationBuilder(int minutes, int seconds) {
        Intent notificationIntent = new Intent(this, MainActivity.class); // [TODO] When the notification is clicked, it will bring the user to the Main Activity. May want to change this to go to the performing screen.
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        return new NotificationCompat.Builder(this, MyApplication.NOTIFICATION_CHANNEL_NAME)
                .setContentTitle("Timer (" + String.valueOf(headerIndex) + " -> " + String.valueOf(setIndex) + ")")
                .setContentText(getString(R.string.notificationChannelDescription, minutes, seconds))//String.valueOf(minutes) + ":" + String.valueOf(seconds))
                .setSmallIcon(R.mipmap.temp_ic_pause_asset)//R.drawable.notification_icon_24dp) //[TODO] This temporarily uses a mipmap instead of a drawable/vector image because the android emulator errors in API 23 and below. Google should fix this later, so wait for that.
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
    }

    // Stops timer and resets some values.
    private void stopTimer() { // [TODO] notification is not being destroyed when finished.
        if (timer != null && timerInProgress) {
            timer.cancel();
            destroyNotification();
        }

        timerInProgress = false;
        stopForeground(true);
    }

    // Destroy notification.
    private void destroyNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(NOTIFICATION_ID);
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initializeNotificationManager();
            notificationManager.deleteNotificationChannel(getString(R.string.notificationChannelName));
        } //[TODO] implement notifications for build versions < 0reo. Use NotificationManagerCompat.from(this);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopTimer();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void updateNotification(NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public class LocalBinder extends Binder {
        public TimerNotificationService getService() {
            return TimerNotificationService.this;
        }
    }
}
