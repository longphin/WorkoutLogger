package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

// Converts ExerciseType to int and vice versa
public class ExerciseTypeConverter {
    @TypeConverter
    public static ExerciseType IntToExerciseType(Integer val) {
        return (ExerciseType.fromInt(val));
    }

    @TypeConverter
    public Integer ExerciseTypeToInt(ExerciseType et) {
        return (et == null ? null : et.asInt());
    }
}
