package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

public class SetTypeConverter {
    @TypeConverter
    public SetType IntToSetType(Integer val) {
        return (SetType.fromInt(val));
    }

    @TypeConverter
    public Integer SetTypeToInt(SetType st) {
        return (st == null ? null : st.asInt());
    }
}
