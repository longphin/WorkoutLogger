package com.longlife.workoutlogger.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Longphi on 1/3/2018.
 */

public class Routine implements Parcelable {
    private static int IDENTITY = 0;
    private int idRoutine;
    private String name = "new";
    private String description = "";
    // true when the routine is to be performed on certain days
    private boolean[] DaysOfWeek = new boolean[7];
    // alternative to DaysOfWeek, the routine may be performed every x days
    private int DaysToNext;
    private boolean IsPremade;

    public Routine() {
        this.idRoutine = Routine.IDENTITY += 1;
    }

    public Routine(String name, String description)
    {
        this(name, description, false);
    }

    public Routine(String name, String description, boolean IsPremade) {

        this.idRoutine = Routine.IDENTITY += 1;
        this.name = name;
        this.description = description;
        this.IsPremade = IsPremade;
    }

    /*
    * Definitions for Parcelable
     */
    public Routine(Parcel parcel)
    {
        idRoutine = parcel.readInt();
        name = parcel.readString();
        description = parcel.readString();
        IsPremade = parcel.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idRoutine);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeByte((byte) (IsPremade ? 1 : 0));
    }

    public static final Parcelable.Creator<Routine> CREATOR = new Parcelable.Creator<Routine>()
    {

        @Override
        public Routine createFromParcel(Parcel parcel) {
            return(new Routine(parcel));
        }

        @Override
        public Routine[] newArray(int size) {
            return(new Routine[size]);
        }
    };
    /* end Parcelable definitions */

    public String getName() {
        return name;
    }

    public int getIdRoutine() {
        return idRoutine;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
