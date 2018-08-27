package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.longlife.workoutlogger.enums.PerformanceStatus;
import com.longlife.workoutlogger.enums.PerformanceStatusConverter;
import com.longlife.workoutlogger.utils.DateConverter;

import java.util.Date;
import java.util.GregorianCalendar;

import io.reactivex.annotations.NonNull;

/**
 * Created by Longphi on 1/7/2018.
 */
@Entity(foreignKeys = @ForeignKey(entity = RoutineHistory.class, parentColumns = "idRoutineHistory", childColumns = "idRoutineHistory", onDelete = ForeignKey.CASCADE),
	indices = {@Index(value = {"idRoutineHistory"})})
public class RoutineSession
{
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
	
	public RoutineSession()
	{
	
	}
	
	// Getters
	public Long getIdRoutineHistory()
	{
		return idRoutineHistory;
	}
	
	public Long getIdRoutineSession()
	{
		return idRoutineSession;
	}
	
	public PerformanceStatus getPerformanceStatus()
	{
		return performanceStatus;
	}
	
	public Date getSessionDate()
	{
		return sessionDate;
	}
	
	// Setters
	public void setPerformanceStatus(PerformanceStatus performanceStatus)
	{
		this.performanceStatus = performanceStatus;
	}
	
	public void setIdRoutineSession(Long i)
	{
		idRoutineSession = i;
	}
	
	public void setIdRoutineHistory(Long i)
	{
		idRoutineHistory = i;
	}
	
	public void setSessionDate(Date sessionDate)
	{
		this.sessionDate = sessionDate;
	}
	
	// Set the session date as the time as of now.
	@Ignore
	public void setSessionDateNow()
	{
		this.sessionDate = (new GregorianCalendar()).getTime();
	}
}

// Inner Classes
