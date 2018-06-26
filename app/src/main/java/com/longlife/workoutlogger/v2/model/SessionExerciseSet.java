package com.longlife.workoutlogger.v2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

/**
 * Created by Longphi on 1/4/2018.
 */
@Entity(foreignKeys = @ForeignKey(entity = SessionExercise.class, parentColumns = "idSessionExercise", childColumns = "idSessionExercise", onDelete = ForeignKey.CASCADE),
	indices = {@Index(value = {"idSessionExercise"})}
)
public class SessionExerciseSet
{
	@PrimaryKey(autoGenerate = true)
	@NonNull
	private int idSessionExerciseSet;
	private int idSessionExercise;
	private int idExercise; // This makes it easier to get the exercises types.
	
	private Integer reps;
	private Double weights;
	private float rest;
	private float duration;
	
	public SessionExerciseSet()
	{
	
	}
	
	// Getters
	public float getDuration()
	{
		return duration;
	}
	
	public int getIdExercise()
	{
		return (this.idExercise);
	}
	
	public int getIdSessionExercise()
	{
		return idSessionExercise;
	}
	
	public int getIdSessionExerciseSet()
	{
		return idSessionExerciseSet;
	}
	
	public Integer getReps()
	{
		return reps;
	}
	
	public float getRest()
	{
		return rest;
	}
	
	public Double getWeights()
	{
		return weights;
	}
	
	// Setters
	public void setIdSessionExerciseSet(int i)
	{
		idSessionExerciseSet = i;
	}
	
	public void setIdExercise(int i)
	{
		idExercise = i;
	}
	
	public void setIdSessionExercise(int i)
	{
		idSessionExercise = i;
	}
	
	public void setDuration(float duration)
	{
		this.duration = duration;
	}
	
	public void setReps(Integer reps)
	{
		this.reps = reps;
	}
	
	public void setWeights(Double weights)
	{
		this.weights = weights;
	}
	
	public void setRest(float rest)
	{
		this.rest = rest;
	}
}

// Inner Classes
