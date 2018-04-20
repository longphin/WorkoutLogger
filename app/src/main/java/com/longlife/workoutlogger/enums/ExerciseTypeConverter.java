package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

// Converts ExerciseType to int and vice versa
public class ExerciseTypeConverter {
    @TypeConverter
    public static ExerciseType IntToExerciseType(int val) {
        return (ExerciseType.fromInt(val));
    }

    @TypeConverter
    public int ExerciseTypeToInt(ExerciseType et) {
        return (et == null ? null : et.asInt());
    }
}
