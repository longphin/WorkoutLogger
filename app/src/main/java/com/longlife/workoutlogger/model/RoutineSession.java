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
        this.idRoutineSession = (RoutineSession.IDENTITY += 1);
        this.idRoutine = routine.getIdRoutine();

        this.sessionDate = (new GregorianCalendar()).getTime();
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
