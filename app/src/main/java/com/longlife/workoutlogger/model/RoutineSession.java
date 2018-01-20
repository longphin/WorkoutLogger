package com.longlife.workoutlogger.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Longphi on 1/7/2018.
 */

public class RoutineSession implements Parcelable {
    public static final Creator<RoutineSession> CREATOR = new Creator<RoutineSession>() {
        @Override
        public RoutineSession createFromParcel(Parcel in) {
            return new RoutineSession(in);
        }

        @Override
        public RoutineSession[] newArray(int size) {
            return new RoutineSession[size];
        }
    };
    private static int IDENTITY = 0;
    private final int idRoutineSession;
    private final int idRoutine;
    private Date sessionDate = (new GregorianCalendar()).getTime();
    private boolean wasPerformed = false;

    public RoutineSession(Routine routine)
    {
        this.idRoutineSession = IDENTITY += 1;
        this.idRoutine = routine.getIdRoutine();
    }

    // copy constructor
    public RoutineSession(RoutineSession routineSession)
    {
        this.idRoutineSession = IDENTITY += 1;
        this.idRoutine = routineSession.getIdRoutine();
    }

    // makes a blank RoutineSession, to be filled later
    public RoutineSession()
    {
        this.idRoutineSession = -1;
        this.idRoutine = -1;
    }

    // Parcelable definitions
    public RoutineSession(Parcel parcel) {
        this.idRoutineSession = parcel.readInt();
        this.idRoutine = parcel.readInt();
    }

    public boolean getWasPerformed() {
        return wasPerformed;
    }

    public void setWasPerformed(boolean wasPerformed) {
        this.wasPerformed = wasPerformed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.idRoutineSession);
        parcel.writeInt(this.idRoutine);
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

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }
}
