package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;

import io.reactivex.annotations.NonNull;

/**
 * This will be the Routine object.
 */
@Entity
public class Routine
{
	// Incremented value to ensure each Routine gets a unique Id.
	@PrimaryKey(autoGenerate = true)
	@NonNull
	private Long idRoutine;
	// This is the current idRoutineHistory that this routine corresponds to.
	private Long currentIdRoutineHistory;
	@Required
	private String name;
	// Note for routine.
	private String description;
	// Flag for hiding the routine.
	@NonNull
	private boolean hidden = false;
	
	public Routine()
	{
	
	}
	
	// Getters
	public Long getCurrentIdRoutineHistory()
	{
		return currentIdRoutineHistory;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public Long getIdRoutine()
	{
		return idRoutine;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isHidden()
	{
		return hidden;
	}
	
	// Setters
	public void setCurrentIdRoutineHistory(Long currentIdRoutineHistory)
	{
		this.currentIdRoutineHistory = currentIdRoutineHistory;
	}
	
	public void setHidden(boolean hidden)
	{
		this.hidden = hidden;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setIdRoutine(Long idRoutine)
	{
		this.idRoutine = idRoutine;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
}

// Inner Classes
