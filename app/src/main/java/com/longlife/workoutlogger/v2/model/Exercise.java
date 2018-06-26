package com.longlife.workoutlogger.v2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.longlife.workoutlogger.v2.data.Required;
import com.longlife.workoutlogger.v2.enums.ExerciseType;
import com.longlife.workoutlogger.v2.enums.ExerciseTypeConverter;
import com.longlife.workoutlogger.v2.enums.MeasurementType;
import com.longlife.workoutlogger.v2.enums.MeasurementTypeConverter;

import io.reactivex.annotations.NonNull;

/**
 * This will be the Exercise object.
 */

@Entity(indices = {
	@Index(value = {"favorited", "name"}),
	@Index(value = {"premade", "name"}, unique = true)
}
)
public class Exercise
{
	@PrimaryKey(autoGenerate = true)
	@NonNull
	private int idExercise;
	// For Exercises that are copies of the shared database, this will be the idExercise from the shared database.
	private int idExerciseShared;
	@Required
	private String name;
	private String description;
	// Official Exercises will have this flag as true.
	private boolean premade;
	
	private boolean favorited;
	
	@TypeConverters({ExerciseTypeConverter.class})
	private ExerciseType exerciseType; // The type of exercise, such as weight, bodyweight, distance.
	@TypeConverters({MeasurementTypeConverter.class})
	private MeasurementType measurementType; // The measurement of the exercise, such as reps or duration.
	
	public Exercise()
	{
	
	}
	
	public Exercise(String name, String descrip)
	{
		this.name = name;
		this.description = descrip;
	}
	
	// Getters
	public String getDescription()
	{
		return description;
	}
	
	public ExerciseType getExerciseType()
	{
		return exerciseType;
	}
	
	public boolean getFavorited()
	{
		return favorited;
	}
	
	public int getIdExercise()
	{
		
		return idExercise;
	}
	
	public int getIdExerciseShared()
	{
		return idExerciseShared;
	}
	
	public MeasurementType getMeasurementType()
	{
		return measurementType;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean getPremade()
	{
		return premade;
	}
	
	// Setters
	public void setIdExercise(int val)
	{
		idExercise = val;
	}
	
	public void setIdExerciseShared(int idExerciseShared)
	{
		this.idExerciseShared = idExerciseShared;
	}
	
	public void setPremade(boolean premade)
	{
		this.premade = premade;
	}
	
	public void setFavorited(boolean favorited)
	{
		this.favorited = favorited;
	}
	
	public void setExerciseType(ExerciseType exerciseType)
	{
		this.exerciseType = exerciseType;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setMeasurementType(MeasurementType measurementType)
	{
		this.measurementType = measurementType;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	// Overrides
	@Override
	public String toString()
	{
		return getName();
	}
}

// Inner Classes
