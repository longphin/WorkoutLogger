package com.longlife.workoutlogger.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This will be the Exercise object.
 */

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
    private static int IDENTITY = 0;

    // Create a standard Exercise with a unique Id.
    public Exercise() {
        super(IDENTITY += 1);
    }

    // Create an Exercise with given a name and description.
    public Exercise(String name, String description) {
        this(name, description, false);
    }

    // Create a premade Exercise with a given name and description.
    public Exercise(String name, String description, boolean IsPremade) {
        super(IDENTITY += 1, IsPremade);
        setName(name);
        setDescription(description);

        setName(name);
        setDescription(description);
        setIsPremade(IsPremade);
    }

    // Used to set the name and description for an existing idExercise
    public Exercise(int idExercise, String name, String description) {
        super(idExercise, false);
        setName(name);
        setDescription(description);
    }

    // Used to create an Exercise to Parcel.
    public Exercise(Parcel parcel) {
        super(parcel.readInt());
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
        parcel.writeString(getName());
        parcel.writeString(getDescription());
        parcel.writeByte((byte) (getIsPremade() ? 1 : 0));
    }
}
