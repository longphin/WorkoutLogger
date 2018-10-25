package com.longlife.workoutlogger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.longlife.workoutlogger.utils.Format;
import com.longlife.workoutlogger.utils.TimeHolder;
import com.longlife.workoutlogger.view.MainActivity;

public class TimerNotificationService
        extends Service {
    public static final String EXTRA_MINUTES = "restMinutes";
    public static final String EXTRA_SECONDS = "restSeconds";
    private static int NOTIFICATION_ID = 49; // This can be anything, I believe.

    private CountDownTimer timer;
    private boolean timerInProgress;
    private int minutes;
    private int seconds;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopTimer();

        minutes = intent.getIntExtra(EXTRA_MINUTES, 0);
        seconds = intent.getIntExtra(EXTRA_SECONDS, 0);

        initializeNotificationBuilder();
        Notification notification = notificationBuilder.build();
        startForeground(NOTIFICATION_ID, notification);

        startTimer(Format.convertToMilliseconds(minutes, seconds));

        return START_NOT_STICKY; // [TODO] Need to learn about the different types and make sure what happens when the system destroys our notification.
    }

    private void stopTimer() {
        if (timer != null && timerInProgress) {
            timer.cancel();
        }

        timerInProgress = false;
    }

    private void initializeNotificationBuilder() {
        if (notificationBuilder == null) {
            Intent notificationIntent = new Intent(this, MainActivity.class); // [TODO] When the notification is clicked, it will bring the user to the Main Activity. May want to change this to go to the performing screen.
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notificationChannelName))
                    .setContentTitle("Timer")
                    .setContentText(getString(R.string.notificationChannelDescription, minutes, seconds))//String.valueOf(minutes) + ":" + String.valueOf(seconds))
                    .setSmallIcon(R.drawable.notification_icon_24dp)
                    .setContentIntent(pendingIntent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTimer(long durationInMillis) {
        timer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                initializeNotificationManager();
                initializeNotificationBuilder();

                TimeHolder timeUntilFinished = Format.getMinutesFromMillis(millisUntilFinished);
                notificationManager.notify(NOTIFICATION_ID,
                        notificationBuilder.setContentText(getString(R.string.notificationChannelDescription, timeUntilFinished.getMinutes(), timeUntilFinished.getSeconds())).build()
                );

            }

            @Override
            public void onFinish() {
                stopSelf(); // Stop the service when the timer is finished.
            }
        }.start();

        timerInProgress = true;
    }

    private void initializeNotificationManager() {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopTimer();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initializeNotificationManager();
            notificationManager.deleteNotificationChannel(getString(R.string.notificationChannelName));
        }
    }
}
