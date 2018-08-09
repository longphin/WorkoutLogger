package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;
import com.longlife.workoutlogger.enums.ExerciseType;
import com.longlife.workoutlogger.enums.ExerciseTypeConverter;
import com.longlife.workoutlogger.enums.MeasurementType;
import com.longlife.workoutlogger.enums.MeasurementTypeConverter;

import io.reactivex.annotations.NonNull;

/**
 * This will be the Exercise object.
 */

@Entity(indices = {
	@Index(value = {"favorited", "name"}),
	@Index(value = {"hidden", "name"})
}
)
public class Exercise
	implements Parcelable
{
	@Ignore
	public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>()
	{
		
		// Overrides
		@Override
		public Exercise createFromParcel(Parcel parcel)
		{
			return new Exercise(parcel);
		}
		
		@Override
		public Exercise[] newArray(int i)
		{
			return new Exercise[i];
		}
	};
	// Name for exercise.
	@Required
	private String name;
	@PrimaryKey
	@NonNull
	private Long idExercise;
	// This is the idExerciseHistory that this current exercise corresponds to.
	private Long currentIdExerciseHistory;
	// Note for the exercise.
	private String description;
	// Flag to indicate whether exercise is favorited.
	private boolean favorited;
	// Flag to indicate whether exercise is hidden.
	@NonNull
	private boolean hidden = false;
	// Type of exercise, used to determine how the exercise should be recorded.
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
	
	@Ignore
	private Exercise(Parcel parcel)
	{
		idExercise = parcel.readLong();
		name = parcel.readString();
	}
	
	// Overrides
	@Override
	public String toString()
	{
		return getName();
	}
	
	@Ignore
	@Override
	public int describeContents()
	{
		return 0;
	}
	
	@Ignore
	@Override
	public void writeToParcel(Parcel parcel, int flags)
	{
		parcel.writeLong(idExercise);
		parcel.writeString(name);
	}
	
	// Getters
	public Long getCurrentIdExerciseHistory()
	{
		return currentIdExerciseHistory;
	}
	
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
	
	public Long getIdExercise()
	{
		
		return idExercise;
	}
	
	public MeasurementType getMeasurementType()
	{
		return measurementType;
	}
	
	// Overrides;
	
	public String getName()
	{
		return name;
	}
	
	public boolean isHidden(){return hidden;}
	
	// Setters
	public void setCurrentIdExerciseHistory(Long currentIdExerciseHistory)
	{
		this.currentIdExerciseHistory = currentIdExerciseHistory;
	}
	
	public void setIdExercise(Long val)
	{
		idExercise = val;
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
	
	public void setHidden(boolean b){hidden = b;}
}

// Inner Classes
