package com.longlife.workoutlogger.enums;

import android.text.InputType;

import com.longlife.workoutlogger.v2.enums.ExerciseType;
import com.longlife.workoutlogger.v2.enums.MeasurementType;

import java.util.HashMap;
import java.util.Map;

/**
 * Static definition codes used to interact between Exercise to Routines.
 */
public class ExerciseRequestCode {
    // Request code to send to Exercise.
    private static final int REQUEST_EXERCISE = 1;
    // The Parcel name used when going back from the Exercise to the Routine.
    private static final String REQUEST_EXERCISE_OK_PARCEL = "ExerciseToRoutine";
    private static final Map<ExerciseType, Integer> ExerciseTypeMap = InitializeExerciseTypes();
    private static final Map<MeasurementType, Integer> MeasurementTypeMap = InitializeMeasurementTypes();

    private static Map<ExerciseType, Integer> InitializeExerciseTypes() {
        Map<ExerciseType, Integer> map = new HashMap<>();
        map.put(ExerciseType.WEIGHT, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        map.put(ExerciseType.BODYWEIGHT, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        map.put(ExerciseType.DISTANCE, InputType.TYPE_CLASS_TEXT);

        return (map);
    }

    private static Map<MeasurementType, Integer> InitializeMeasurementTypes() {
        Map<MeasurementType, Integer> map = new HashMap<>();
        map.put(MeasurementType.REP, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        map.put(MeasurementType.DURATION, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        return (map);
    }

    public static int getExerciseTypeInputType(ExerciseType et) {
        return (ExerciseTypeMap.get(et));
    }

    public static int getMeasurementTypeInputType(MeasurementType mt) {
        return (MeasurementTypeMap.get(mt));
    }

    public static String getRequestExercise_OK_Parcel() {
        return REQUEST_EXERCISE_OK_PARCEL;
    }

    public static int getRequestExercise() {
        return REQUEST_EXERCISE;
    }
}
