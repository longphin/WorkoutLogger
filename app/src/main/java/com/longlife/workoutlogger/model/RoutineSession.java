package com.longlife.workoutlogger.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Longphi on 1/7/2018.
 */

public class RoutineSession implements Parcelable {
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

    // Parcelable definitions
    public RoutineSession(Parcel parcel) {
        this.idRoutineSession = parcel.readInt();
        this.idRoutine = parcel.readInt();
    }

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
}
