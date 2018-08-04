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
		// [TODO] This cascade is not preferred. Rather than deleting exercises, we should maybe hide them. Only delete exercises when they have no references.
		@ForeignKey(entity = Exercise.class, parentColumns = "idExercise", childColumns = "idExercise", onDelete = ForeignKey.CASCADE)
	},
	indices = {
		@Index(value = {"idRoutineSession", "idExercise"}),
		@Index(value = {"idRoutineSession"}),
		@Index(value = {"idExercise"})
	}
)
public class SessionExercise
{
	@PrimaryKey(autoGenerate = true)
	@NonNull
	private int idSessionExercise;
	private int idRoutineSession;
	private int idExercise;
	
	public SessionExercise()
	{
	
	}
	
	@Ignore
	public SessionExercise(int idExercise, int idRoutineSession)
	{
		this.idExercise = idExercise;
		this.idRoutineSession = idRoutineSession;
	}
	
	// Getters
	
	public int getIdExercise()
	{
		return idExercise;
	}
	
	public int getIdRoutineSession()
	{
		return idRoutineSession;
	}
	
	public int getIdSessionExercise()
	{
		return idSessionExercise;
	}
	
	// Setters
	public void setIdRoutineSession(int i)
	{
		idRoutineSession = i;
	}
	
	public void setIdSessionExercise(int i)
	{
		idSessionExercise = i;
	}
	
	public void setIdExercise(int i)
	{
		idExercise = i;
	}
}

// Inner Classes
