package com.longlife.workoutlogger.enums;

/**
 * Static definition codes used to interact between Exercise to Routines.
 */
public class ExerciseRequestCode {
    // Request code to send to Exercise.
    private static final int REQUEST_EXERCISE = 1;
    // The Parcel name used when going back from the Exercise to the Routine.
    private static final String REQUEST_EXERCISE_OK_PARCEL = "ExerciseToRoutine";

    public static String getRequestExercise_OK_Parcel() {
        return REQUEST_EXERCISE_OK_PARCEL;
    }

    public static int getRequestExercise() {
        return REQUEST_EXERCISE;
    }
}
