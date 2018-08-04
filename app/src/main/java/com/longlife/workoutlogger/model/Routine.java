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
	private int idRoutine;
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
	public boolean isHidden()
	{
		return hidden;
	}
	
	// Setters
	public void setHidden(boolean hidden)
	{
		this.hidden = hidden;
	}
	public String getDescription()
	{
		return description;
	}
	
	public int getIdRoutine()
	{
		return idRoutine;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setIdRoutine(int i)
	{
		idRoutine = i;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
}

// Inner Classes
