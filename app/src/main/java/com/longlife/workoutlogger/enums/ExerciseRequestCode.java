package com.longlife.workoutlogger.enums;

/**
 * Created by Longphi on 1/14/2018.
 */

public class ExerciseRequestCode {
    private static final int REQUEST_EXERCISE = 1; // request code to send to exercise
    private static final String REQUEST_EXERCISE_OK_PARCEL = "ExerciseToRoutine";

    public static String getRequestExercise_OK_Parcel() {
        return REQUEST_EXERCISE_OK_PARCEL;
    }

    public static int getRequestExercise() {
        return REQUEST_EXERCISE;
    }
}
