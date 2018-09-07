package com.longlife.workoutlogger.model;

import android.arch.persistence.room.*;
import com.longlife.workoutlogger.enums.PerformanceStatus;
import com.longlife.workoutlogger.enums.PerformanceStatusConverter;
import com.longlife.workoutlogger.utils.DateConverter;
import io.reactivex.annotations.NonNull;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Longphi on 1/7/2018.
 */
@Entity(foreignKeys = @ForeignKey(entity = RoutineHistory.class, parentColumns = "idRoutineHistory", childColumns = "idRoutineHistory", onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"idRoutineHistory"})})
public class RoutineSession {
    @PrimaryKey
    @NonNull
    private Long idRoutineSession;
    private Long idRoutineHistory;
    @TypeConverters({DateConverter.class})
    private Date sessionDate;
    // Flag for determining if the session was performed. If the last session for a routine was performed,
    // then we need to create a new session.
    @NonNull
    @TypeConverters({PerformanceStatusConverter.class})
    private PerformanceStatus performanceStatus = PerformanceStatus.NEW;

    public RoutineSession() {

    }


    public Long getIdRoutineHistory() {
        return idRoutineHistory;
    }

    public void setIdRoutineHistory(Long i) {
        idRoutineHistory = i;
    }

    public Long getIdRoutineSession() {
        return idRoutineSession;
    }

    public void setIdRoutineSession(Long i) {
        idRoutineSession = i;
    }

    public PerformanceStatus getPerformanceStatus() {
        return performanceStatus;
    }


    public void setPerformanceStatus(PerformanceStatus performanceStatus) {
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


