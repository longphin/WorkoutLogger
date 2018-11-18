package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

public class MuscleConverter {
    @TypeConverter
    public Muscle IntToEnum(Integer val) {
        return (Muscle.fromInt(val));
    }

    @TypeConverter
    public Integer EnumToInt(Muscle e) {
        return (e == null ? null : e.asInt());
    }
}
