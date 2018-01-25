package com.longlife.workoutlogger.enums;

/**
 * Static definition codes used to interact between Routine to Routines list.
 */
public class RoutineRequestCode {
    // Request code to send to Routine.
    private static final int REQUEST_ROUTINE = 1;
    // The Parcel name used when going back from the Exercise to the Routine for the OK status.
    private static final String REQUEST_ROUTINE_OK_PARCEL = "RoutineToRoutines";
    // The Parcel name used when going back from the Exercise to the Routine for the Cancel status.
    private static final String REQUEST_ROUTINE_CANCEL_PARCEL = "RoutineToRoutines";

    /**
     * The Parcel name used when going back from the Exercise to the Routine for the OK status.
     *
     * @return Parcel name.
     */
    public static String getRequestRoutine_OK_Parcel() {
        return REQUEST_ROUTINE_OK_PARCEL;
    }

    /**
     * The Parcel name used when going back from the Exercise to the Routine for the Cancel status.
     *
     * @return Parcel name.
     */
    public static String getRequestRoutine_Cancel_Parcel() {
        return REQUEST_ROUTINE_CANCEL_PARCEL;
    }

    /** Request code to send to Routine.
     * @return Parcel name.
     */
    public static int getRequestRoutine() {
        return REQUEST_ROUTINE;
    }
}
