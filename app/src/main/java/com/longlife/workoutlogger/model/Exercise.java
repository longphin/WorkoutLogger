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
import com.longlife.workoutlogger.utils.Conversions;

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
	// Getters
	
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
	@NonNull
	private boolean hidden = false;
	
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
	@Override
	public String toString()
	{
		return getName();
	}
	
	@Ignore
	private Exercise(Parcel parcel)
	{
		idExercise = parcel.readInt();
		name = parcel.readString();
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
	
	// Overrides;
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
	
	public boolean isHidden(){return hidden;}
	
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
		parcel.writeInt(idExercise);
		parcel.writeString(name);
	}
	
	@Ignore
	public void setIdExercise(Long idExercise)
	{
		this.idExercise = Conversions.safeLongToInt(idExercise);
	}
	public void setHidden(boolean b){hidden = b;}
}

// Inner Classes
