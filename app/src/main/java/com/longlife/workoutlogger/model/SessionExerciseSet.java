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
        implements Parcelable {
    @Ignore
    public static final Parcelable.Creator<SessionExerciseSet> CREATOR = new Parcelable.Creator<SessionExerciseSet>() {

        @Override
        public SessionExerciseSet createFromParcel(Parcel parcel) {
            return new SessionExerciseSet(parcel);
        }

        @Override
        public SessionExerciseSet[] newArray(int i) {
            return new SessionExerciseSet[i];
        }
    };
    @PrimaryKey
    @NonNull
    private Long idSessionExerciseSet;
    private Long idSessionExercise;
    private Integer reps;
    private Double weights;

    @NonNull
    private int restMinutes = 0;
    @NonNull
    private int restSeconds = 0;
    private float duration;

    @Ignore
    private SessionExerciseSet(Parcel parcel) {
        idSessionExerciseSet = parcel.readLong();
        restMinutes = parcel.readInt();
        restSeconds = parcel.readInt();
    }

    public SessionExerciseSet() {
    }

    public SessionExerciseSet(Long idSessionExercise) {
        this.idSessionExercise = idSessionExercise;
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }


    @Ignore
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(idSessionExerciseSet);
        parcel.writeInt(restMinutes);
        parcel.writeInt(restSeconds);
    }


    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public Long getIdSessionExercise() {
        return idSessionExercise;
    }

    public void setIdSessionExercise(Long i) {
        idSessionExercise = i;
    }

    public Long getIdSessionExerciseSet() {
        return idSessionExerciseSet;
    }

    public void setIdSessionExerciseSet(Long i) {
        idSessionExerciseSet = i;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    @NonNull
    public int getRestMinutes() {
        return restMinutes;
    }


    public void setRestMinutes(int restMinutes) {
        this.restMinutes = restMinutes;
    }

    @NonNull
    public int getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(int restSeconds) {
        this.restSeconds = restSeconds;
    }

    public Double getWeights() {
        return weights;
    }

    public void setWeights(Double weights) {
        this.weights = weights;
    }

    @Ignore
    public void setRest(int restMinutes, int restSeconds) {
        this.restMinutes = restMinutes;
        this.restSeconds = restSeconds;
    }
}


