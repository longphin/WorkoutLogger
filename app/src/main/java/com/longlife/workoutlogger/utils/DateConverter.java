package com.longlife.workoutlogger.utils;

import android.arch.persistence.room.TypeConverter;

import com.longlife.workoutlogger.data.UserProfile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private static DateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT, UserProfile.getLocale());

    @TypeConverter
    public static Date StrToDate(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return (null);
        } else {
            return (null);
        }
    }

    @TypeConverter
    public static String DateToStr(Date date) {
        if (date != null) {
            return (date.toString());
        }
        return (null);
    }
}