package com.longlife.workoutlogger.utils;

import android.content.Context;

public class GetResource {
    public static String getStringResource(Context context, int resourceId) {
        return context.getResources().getString(resourceId);
    }
}
