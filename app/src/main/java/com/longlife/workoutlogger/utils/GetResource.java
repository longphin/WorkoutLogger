/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/23/18 5:59 PM.
 */

package com.longlife.workoutlogger.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.ConfigurationCompat;

public class GetResource {
    public static String getStringResource(Context context, int resourceId) {
        return context.getResources().getString(resourceId);
    }

    public static String getStringResource(Context context, int resourceId, @Nullable Object... formatArgs) {
        return context.getResources().getString(resourceId, formatArgs);
    }

    public static int getIntResource(Context context, int resourceId) {
        return context.getResources().getInteger(resourceId);
    }

    @NonNull
    public Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }

    // Get locale-dependent resources.
    public Locale getCurrentLocale(Context context) {
        return ConfigurationCompat.getLocales(context.getResources().getConfiguration()).get(0);
    }
}
