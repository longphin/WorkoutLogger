package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import com.longlife.workoutlogger.enums.ExerciseType;
import com.longlife.workoutlogger.enums.MeasurementType;

/**
 * This will be the Exercise object.
 */

@Entity
public class Exercise extends ExerciseAbstract implements Parcelable {
    // For Parcel.
    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel parcel) {
            return new Exercise(parcel);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
    // Used to give a unique identifier to each instance if necessary.
    @Ignore
    private static int IDENTITY = 0;

    // Create a standard Exercise with a unique Id.
    public Exercise() {
        super(IDENTITY += 1, ExerciseType.WEIGHT, MeasurementType.REP.REP);
    }

    // Create an Exercise with given a name and description.
    public Exercise(String name, String description, ExerciseType et, MeasurementType mt) {
        this(name, description, et, mt, false);
    }

    // Create a premade Exercise with a given name and description.
    public Exercise(String name, String description, ExerciseType et, MeasurementType mt, boolean IsPremade) {
        super(IDENTITY += 1, ExerciseType.WEIGHT, MeasurementType.REP.REP, IsPremade);
        setName(name);
        setDescription(description);

        setName(name);
        setDescription(description);
        setIsPremade(IsPremade);
    }

    // Used to set the name and description for an existing idExercise
    public Exercise(int idExercise, String name, String description, ExerciseType et, MeasurementType mt) {
        super(idExercise, et, mt, false);
        setName(name);
        setDescription(description);
    }

    // Used to create an Exercise to Parcel.
    public Exercise(Parcel parcel) {
        super(parcel.readInt(),
                ExerciseType.values()[parcel.readInt()], // the parcel is an int, so find the corresponding ExerciseType
                MeasurementType.values()[parcel.readInt()]); // the parcel is an int, so find the corresponding MeasurementType

        setName(parcel.readString());
        setDescription(parcel.readString());
        setIsPremade(parcel.readByte() != 0);
    }

    // For Parcel.
    @Override
    public int describeContents() {
        return 0;
    }

    // Write fields to Parcel.
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getIdExercise());
        parcel.writeInt(getExerciseType().ordinal()); // parcel the exerciseType as an int
        parcel.writeInt(getMeasurementType().ordinal()); // parcel the measurementType as an int
        parcel.writeString(getName());
        parcel.writeString(getDescription());
        parcel.writeByte((byte) (getIsPremade() ? 1 : 0));
    }
}
