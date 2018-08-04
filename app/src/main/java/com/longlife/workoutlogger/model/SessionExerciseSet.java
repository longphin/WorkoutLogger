package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import io.reactivex.annotations.NonNull;

/**
 * Created by Longphi on 1/4/2018.
 */
@Entity(foreignKeys = @ForeignKey(entity = SessionExercise.class, parentColumns = "idSessionExercise", childColumns = "idSessionExercise", onDelete = ForeignKey.CASCADE),
	indices = {@Index(value = {"idSessionExercise"})}
)
public class SessionExerciseSet
	implements Parcelable
{
	@Ignore
	public static final Parcelable.Creator<SessionExerciseSet> CREATOR = new Parcelable.Creator<SessionExerciseSet>()
	{
		// Overrides
		@Override
		public SessionExerciseSet createFromParcel(Parcel parcel)
		{
			return new SessionExerciseSet(parcel);
		}
		
		@Override
		public SessionExerciseSet[] newArray(int i)
		{
			return new SessionExerciseSet[i];
		}
	};
	@PrimaryKey(autoGenerate = true)
	@NonNull
	private Long idSessionExerciseSet;
	private Long idSessionExercise;
	private Integer reps;
	private Double weights;
	private float rest;
	private float duration;
	
	public SessionExerciseSet()
	{
	
	}
	
	@Ignore
	private SessionExerciseSet(Parcel parcel)
	{
		idSessionExerciseSet = parcel.readLong();
		rest = parcel.readFloat();
	}
	
	// Overrides
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
		parcel.writeLong(idSessionExerciseSet);
		parcel.writeFloat(rest);
	}
	
	// Getters
	public float getDuration()
	{
		return duration;
	}
	
	public Long getIdSessionExercise()
	{
		return idSessionExercise;
	}
	
	public Long getIdSessionExerciseSet()
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
	public void setIdSessionExerciseSet(Long i)
	{
		idSessionExerciseSet = i;
	}
	
	public void setIdSessionExercise(Long i)
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
