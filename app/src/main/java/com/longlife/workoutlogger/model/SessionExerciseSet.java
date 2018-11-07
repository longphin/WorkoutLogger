package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.longlife.workoutlogger.enums.SetType;
import com.longlife.workoutlogger.enums.SetTypeConverter;
import com.longlife.workoutlogger.enums.WeightUnitTypes;


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
    private Long idSessionExerciseSet;
    private Long idSessionExercise;
    @Nullable
    private Integer reps;
    @Nullable
    private Double weights;

    private int restMinutes = 0;
    private int restSeconds = 0;
    private float duration;

    @NonNull
    @TypeConverters({SetTypeConverter.class})
    private SetType type = SetType.REGULAR;

    // The type of a unit.
    private int weightUnit = WeightUnitTypes.getDefault();

    // Indicate whether the set was performed.
    private boolean performed = false;

    public boolean isPerformed() {
        return performed;
    }

    public void setPerformed(boolean performed) {
        this.performed = performed;
    }

    public int getWeightUnit() {
        return weightUnit;
    }

    @NonNull
    public SetType getType() {
        return type;
    }

    public void setType(SetType type) {
        this.type = type;
    }

    public void setWeightUnit(int weightUnit) {
        this.weightUnit = weightUnit;
    }

    @Ignore
    private SessionExerciseSet(Parcel parcel) {
        idSessionExerciseSet = parcel.readLong();
        restMinutes = parcel.readInt();
        restSeconds = parcel.readInt();
    }

    public SessionExerciseSet() {
    }

    @Ignore
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

    @Nullable
    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public int getRestMinutes() {
        return restMinutes;
    }


    public void setRestMinutes(int restMinutes) {
        this.restMinutes = restMinutes;
    }

    public int getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(int restSeconds) {
        this.restSeconds = restSeconds;
    }

    @Nullable
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


