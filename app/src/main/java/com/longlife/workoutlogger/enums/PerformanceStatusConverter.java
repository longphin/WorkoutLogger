package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

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

