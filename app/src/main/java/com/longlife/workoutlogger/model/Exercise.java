package com.longlife.workoutlogger.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Longphi on 1/3/2018.
 */

public class Exercise extends ExerciseAbstract implements Parcelable {
    private static int IDENTITY = 0;

    public Exercise()
    {
        super(IDENTITY += 1);
    }

    public Exercise(String name, String description)
    {
        this(name, description, false);
    }
    public Exercise(String name, String description, boolean IsPremade)
    {
        super(IDENTITY += 1, IsPremade);
        setName(name);
        setDescription(description);

        setName(name);
        setDescription(description);
        setIsPremade(IsPremade);
    }

    // used to set the name and description for an existing idExercise
    public Exercise(int idExercise, String name, String description)
    {
        super(idExercise, false);
        setName(name);
        setDescription(description);
    }
    // definitions for Parcelable
    public Exercise(Parcel parcel)
    {
        super(parcel.readInt());
        setName(parcel.readString());
        setDescription(parcel.readString());
        setIsPremade(parcel.readByte() != 0);
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getIdExercise());
        parcel.writeString(getName());
        parcel.writeString(getDescription());
        parcel.writeByte((byte)(getIsPremade() ? 1 : 0));
    }
}
