package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Exercise.class, parentColumns = "idExercise", childColumns = "idExercise", onDelete = ForeignKey.CASCADE),
	indices = {@Index(value = {"idExercise"})})
public class ExerciseHistory
{
	@PrimaryKey
	@NonNull
	private Long idExerciseHistory;
	@NonNull
	private Long idExercise;
	@NonNull
	private String name;
	private String note;
	
	public ExerciseHistory(){}
	
	@Ignore
	public ExerciseHistory(Exercise ex)
	{
		this.idExercise = ex.getIdExercise();
		this.name = ex.getName();
	}
	
	// Getters
	public Long getIdExercise()
	{
		return idExercise;
	}
	
	public Long getIdExerciseHistory()
	{
		return idExerciseHistory;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getNote()
	{
		
		return note;
	}
	
	// Setters
	public void setIdExerciseHistory(Long idExerciseHistory)
	{
		this.idExerciseHistory = idExerciseHistory;
	}
	
	public void setIdExercise(Long idExercise)
	{
		this.idExercise = idExercise;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setNote(String note)
	{
		this.note = note;
	}
}
