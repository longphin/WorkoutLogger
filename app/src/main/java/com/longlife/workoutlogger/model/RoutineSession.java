package com.longlife.workoutlogger.model;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Longphi on 1/7/2018.
 */

public class RoutineSession {
    private static int IDENTITY = 0;
    private final int idRoutineSession;
    private final int idRoutine;
    private Date sessionDate;

    public RoutineSession(Routine routine)
    {
        this.idRoutineSession = IDENTITY += 1;
        this.idRoutine = routine.getIdRoutine();

        this.sessionDate = (new GregorianCalendar()).getTime();
    }

    // copy constructor
    public RoutineSession(RoutineSession routineSession)
    {
        this.idRoutineSession = IDENTITY += 1;
        this.idRoutine = routineSession.getIdRoutine();
        this.sessionDate = (new GregorianCalendar()).getTime();
    }

    // makes a blank RoutineSession, to be filled later
    public RoutineSession()
    {
        this.idRoutineSession = -1;
        this.idRoutine = -1;
    }

    public int getIdRoutineSession() {
        return idRoutineSession;
    }

    public int getIdRoutine() {
        return idRoutine;
    }

    public Date getSessionDate() {
        return sessionDate;
    }
}
