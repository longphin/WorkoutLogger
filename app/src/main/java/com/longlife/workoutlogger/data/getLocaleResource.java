package com.longlife.workoutlogger.data;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.Locale;

public class getLocaleResource {
    @NonNull
    public Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }
}
