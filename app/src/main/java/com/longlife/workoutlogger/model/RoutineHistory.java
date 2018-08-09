package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Routine.class, parentColumns = "idRoutine", childColumns = "idRoutine", onDelete = ForeignKey.CASCADE),
	indices = {@Index(value = {"idRoutine"})})
public class RoutineHistory
{
	@PrimaryKey
	@NonNull
	private Long idRoutineHistory;
	@NonNull
	private Long idRoutine;
	@NonNull
	private String name;
	private String note;
	
	public RoutineHistory(){}
	
	@Ignore
	public RoutineHistory(Routine r)
	{
		this.idRoutine = r.getIdRoutine();
		this.name = r.getName();
	}
	
	// Getters
	public Long getIdRoutine()
	{
		return idRoutine;
	}
	
	public Long getIdRoutineHistory()
	{
		return idRoutineHistory;
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
	public void setIdRoutineHistory(Long idRoutineHistory)
	{
		this.idRoutineHistory = idRoutineHistory;
	}
	
	public void setIdRoutine(Long idRoutine)
	{
		this.idRoutine = idRoutine;
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
