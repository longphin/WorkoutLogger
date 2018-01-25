package com.longlife.workoutlogger.model;

/**
 * Created by Longphi on 1/4/2018.
 */

public class SessionExercise {
    private static int IDENTITY = 0;
    private final int idSessionExercise;
    private final int idRoutineSession;
    private final int idExercise;
    private int displayOrder;
    private int defaultNumberOfSets; // [TODO] this will updated to be the number of sets in the previous session
    private long defaultRestTime; // [TODO] this will be used to give a default rest time

    public SessionExercise(RoutineSession routineSession, Exercise exercise)
    {
        this.idSessionExercise = IDENTITY += 1;
        this.idRoutineSession = routineSession.getIdRoutineSession();
        this.idExercise = exercise.getIdExercise();
    }

    // copy constructor
    public SessionExercise(RoutineSession routineSession, SessionExercise sessionExerciseToCopy)
    {
        this.idSessionExercise = IDENTITY += 1;
        this.idRoutineSession = routineSession.getIdRoutineSession();
        this.idExercise = sessionExerciseToCopy.getIdExercise();
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getDefaultNumberOfSets() {
        return defaultNumberOfSets;
    }

    public void setDefaultNumberOfSets(int defaultNumberOfSets) {
        this.defaultNumberOfSets = defaultNumberOfSets;
    }

    public long getDefaultRestTime() {
        return defaultRestTime;
    }

    public void setDefaultRestTime(long defaultRestTime) {
        this.defaultRestTime = defaultRestTime;
    }

    public int getIdRoutineSession() {
        return idRoutineSession;
    }

    public int getIdSessionExercise() {
        return idSessionExercise;
    }

    public int getIdExercise() {
        return idExercise;
    }
}
