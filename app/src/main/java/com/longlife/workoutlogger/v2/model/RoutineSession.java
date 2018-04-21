package com.longlife.workoutlogger.v2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.longlife.workoutlogger.enums.DateConverter;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Longphi on 1/7/2018.
 */
@Entity(foreignKeys = @ForeignKey(entity = Routine.class, parentColumns = "idRoutine", childColumns = "idRoutine", onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"idRoutine"})})
public class RoutineSession {
    @PrimaryKey
    private int idRoutineSession;
    private int idRoutine;
    @TypeConverters({DateConverter.class})
    private Date sessionDate = (new GregorianCalendar()).getTime();
    private boolean wasPerformed = false;

    public RoutineSession() {

    }

    public boolean getWasPerformed() {
        return wasPerformed;
    }

    public void setWasPerformed(boolean wasPerformed) {
        this.wasPerformed = wasPerformed;
    }

    public int getIdRoutineSession() {
        return idRoutineSession;
    }

    public void setIdRoutineSession(int i) {
        idRoutineSession = i;
    }

    public int getIdRoutine() {
        return idRoutine;
    }

    public void setIdRoutine(int i) {
        idRoutine = i;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }
}
