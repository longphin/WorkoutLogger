package com.longlife.workoutlogger.view;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.Format;

public class TimerNotificationService
        extends Service {
    public static final String EXTRA_MINUTES = "restMinutes";
    public static final String EXTRA_SECONDS = "restSeconds";
    private static int NOTIFICATION_ID = 49; // This can be anything, I believe.

    private CountDownTimer timer;
    private boolean timerInProgress;
    private long timeRemaining;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int minutes = intent.getIntExtra(EXTRA_MINUTES, 0);
        int seconds = intent.getIntExtra(EXTRA_SECONDS, 0);

        Intent notificationIntent = new Intent(this, MainActivity.class); // [TODO] When the notification is clicked, it will bring the user to the Main Activity. May want to change this to go to the performing screen.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, MyApplication.NOTIFICATION_CHANNEL_NAME)
                .setContentTitle("Timer")
                .setContentText(String.valueOf(minutes) + ":" + String.valueOf(seconds))
                .setSmallIcon(R.drawable.notification_icon_24dp)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        startTimer(Format.convertToMilliseconds(minutes, seconds));

        return START_NOT_STICKY; // [TODO] Need to learn about the different types and make sure what happens when the system destroys our notification.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                timeRemaining = millisUntilFinished;
                // [TODO] send this value to any observers.
            }

            @Override
            public void onFinish() {
                stopSelf(); // Stop the service when the timer is finished.
            }
        }.start();

        timerInProgress = true;
    }
}
