package com.longlife.workoutlogger.v2.data;

import java.util.Locale;

// [TODO] These will editable from a profile page once implemented.
public class UserProfile {
    private static Locale locale = Locale.US;

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale l) {
        locale = l;
    }
}
