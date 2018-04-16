package com.longlife.workoutlogger.utils;

import android.arch.persistence.room.TypeConverter;

import com.longlife.workoutlogger.enums.ExerciseRequestCode;
import com.longlife.workoutlogger.model.Exercise;

import java.util.Comparator;

/**
 * Created by Longphi on 2/2/2018.
 */

public class ExerciseComparator implements Comparator<Exercise> {

    @Override
    public int compare(Exercise e1, Exercise e2) {
        // Order by name.
        int res = e1.getName().compareTo(e2.getName());

        // Order by id.
        if (res == 0) return (e1.getIdExercise() >= e2.getIdExercise() ? 1 : -1);
        return (res);
    }

    // Converts ExerciseType to int and vice versa
    @TypeConverter
    public static ExerciseRequestCode.ExerciseType IntToExerciseType(int val) {
        return (ExerciseRequestCode.ExerciseType.fromInt(val));
    }

    @TypeConverter
    public int ExerciseTypeToInt(ExerciseRequestCode.ExerciseType et) {
        return (et == null ? null : et.asInt());
    }

    // Converts MeasurementType to int and vice versa
    @TypeConverter
    public ExerciseRequestCode.MeasurementType IntToMeasurementType(int val) {
        return (ExerciseRequestCode.MeasurementType.fromInt(val));
    }

    @TypeConverter
    public int MeasurementTypeToInt(ExerciseRequestCode.MeasurementType et) {
        return (et == null ? null : et.asInt());
    }
}
