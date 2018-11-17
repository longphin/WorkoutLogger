package com.longlife.workoutlogger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PreloadDataService extends Service {
    public PreloadDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
