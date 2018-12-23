/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {
    //private static DateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT, Profile.getLocale());

    @TypeConverter
    public static Date StrToDate(String value) {
        if (value != null) {
            try {
                return new SimpleDateFormat(Constants.TIME_STAMP_FORMAT).parse(value);//df.parse(value);
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
