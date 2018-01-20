package com.longlife.workoutlogger.enums;

/**
 * Created by Longphi on 1/14/2018.
 */

public class RoutineRequestCode {
    private static final int REQUEST_ROUTINE = 1; // request code to send to exercise
    private static final String REQUEST_ROUTINE_OK_PARCEL = "RoutineToRoutines";
    private static final String REQUEST_ROUTINE_CANCEL_PARCEL = "RoutineToRoutines";

    public static String getRequestRoutine_OK_Parcel() {
        return REQUEST_ROUTINE_OK_PARCEL;
    }

    public static String getRequestRoutine_Cancel_Parcel() {
        return REQUEST_ROUTINE_CANCEL_PARCEL;
    }

    public static int getRequestRoutine() {
        return REQUEST_ROUTINE;
    }
}
