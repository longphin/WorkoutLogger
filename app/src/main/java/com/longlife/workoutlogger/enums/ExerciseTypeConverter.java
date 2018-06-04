package com.longlife.workoutlogger.enums;

import android.arch.persistence.room.TypeConverter;

// Converts ExerciseType to int and vice versa
public class ExerciseTypeConverter {
    @TypeConverter
    public static ExerciseType IntToExerciseType(Long val) {
        return (ExerciseType.fromLong(val));
    }

    @TypeConverter
    public Long ExerciseTypeToInt(ExerciseType et) {
        return (et == null ? null : et.asLong());
    }
}
