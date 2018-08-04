package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

/**
 * Created by Longphi on 1/4/2018.
 */

@Entity(
	foreignKeys = {
		@ForeignKey(entity = RoutineSession.class, parentColumns = "idRoutineSession", childColumns = "idRoutineSession", onDelete = ForeignKey.CASCADE),
		@ForeignKey(entity = ExerciseHistory.class, parentColumns = "idExerciseHistory", childColumns = "idExerciseHistory", onDelete = ForeignKey.CASCADE)
	},
	indices = {
		@Index(value = {"idRoutineSession", "idExerciseHistory"}),
		@Index(value = {"idRoutineSession"}),
		@Index(value = {"idExerciseHistory"})
	}
)
public class SessionExercise
{
	@PrimaryKey(autoGenerate = true)
	@NonNull
	private Long idSessionExercise;
	private Long idRoutineSession;
	private Long idExerciseHistory;
	
	public SessionExercise()
	{
	
	}
	
	@Ignore
	public SessionExercise(Long idExerciseHistory, Long idRoutineSession)
	{
		this.idExerciseHistory = idExerciseHistory;
		this.idRoutineSession = idRoutineSession;
	}
	
	// Getters
	
	public Long getIdExerciseHistory()
	{
		return idExerciseHistory;
	}
	
	public Long getIdRoutineSession()
	{
		return idRoutineSession;
	}
	
	public Long getIdSessionExercise()
	{
		return idSessionExercise;
	}
	
	// Setters
	public void setIdRoutineSession(Long i)
	{
		idRoutineSession = i;
	}
	
	public void setIdSessionExercise(Long i)
	{
		idSessionExercise = i;
	}
	
	public void setIdExerciseHistory(Long i)
	{
		idExerciseHistory = i;
	}
}

// Inner Classes
