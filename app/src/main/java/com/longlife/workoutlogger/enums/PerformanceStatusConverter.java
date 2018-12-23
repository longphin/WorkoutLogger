/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.enums;

import androidx.room.TypeConverter;

public class PerformanceStatusConverter {
    @TypeConverter
    public PerformanceStatus IntToPerformanceStatus(Integer val) {
        return (PerformanceStatus.fromInt(val));
    }

    @TypeConverter
    public Integer PerformanceStatusToInt(PerformanceStatus ps) {
        return (ps == null ? null : ps.asInt());
    }
}

