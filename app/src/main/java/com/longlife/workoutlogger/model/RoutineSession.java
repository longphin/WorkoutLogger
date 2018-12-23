/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.model;

import com.longlife.workoutlogger.enums.PerformanceStatus;
import com.longlife.workoutlogger.enums.PerformanceStatusConverter;
import com.longlife.workoutlogger.utils.DateConverter;

import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

/**
 * Created by Longphi on 1/7/2018.
 */
@Entity(foreignKeys = @ForeignKey(entity = Routine.class, parentColumns = "idRoutine", childColumns = "idRoutine", onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"idRoutine"})})
public class RoutineSession {
    @PrimaryKey
    private Long idRoutineSession;
    @Nullable
    private Long idRoutine;
    @TypeConverters({DateConverter.class})
    private Date sessionDate;
    // Flag for determining if the session was performed. If the last session for a routine was performed,
    // then we need to create a new session.
    @NonNull
    @TypeConverters({PerformanceStatusConverter.class})
    private PerformanceStatus performanceStatus = PerformanceStatus.NEW;

    public RoutineSession() {

    }

    @Nullable
    public Long getIdRoutine() {
        return idRoutine;
    }

    public void setIdRoutine(Long i) {
        idRoutine = i;
    }

    public Long getIdRoutineSession() {
        return idRoutineSession;
    }

    public void setIdRoutineSession(Long i) {
        idRoutineSession = i;
    }

    @NonNull
    public PerformanceStatus getPerformanceStatus() {
        return performanceStatus;
    }


    public void setPerformanceStatus(@NonNull PerformanceStatus performanceStatus) {
        this.performanceStatus = performanceStatus;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    // Set the session date as the time as of now.
    @Ignore
    public void setSessionDateNow() {
        this.sessionDate = (new GregorianCalendar()).getTime();
    }
}


