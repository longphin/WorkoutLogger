package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    private static DateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT, Locale.US);

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
