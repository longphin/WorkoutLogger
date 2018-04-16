package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This will be the Routine object.
 */
@Entity
public class Routine implements Parcelable {
    // For Parcel.
    public static final Parcelable.Creator<Routine> CREATOR = new Parcelable.Creator<Routine>() {

        @Override
        public Routine createFromParcel(Parcel parcel) {
            return (new Routine(parcel));
        }

        @Override
        public Routine[] newArray(int size) {
            return (new Routine[size]);
        }
    };
    // Incremented value to ensure each Routine gets a unique Id.
    @Ignore
    private static int IDENTITY = 0;
    @PrimaryKey
    private int idRoutine;
    private String name = "new";
    private String description = "";
    // For each day of the week, a true value indicates that the Routine will be performed on that day.
    private boolean[] DaysOfWeek = new boolean[7];
    // Alternative to DaysOfWeek, the Routine may be performed every "x" days
    private int DaysToNext;
    // Flag to indicate if the Routine is premade.
    private boolean IsPremade;
    // Order that the Routine is displayed in RoutinesActivity.
    private int displayOrder;

    /**
     * Constructor that only sets the Id.
     */
    public Routine() {
        this.idRoutine = Routine.IDENTITY += 1;
    }

    /**
     * Constructor for non-premade Routines.
     *
     * @param name
     * @param description
     */
    public Routine(String name, String description) {
        this(name, description, false);
    }

    /** Constructor with a chosen IsPremade flag.
     * @param name
     * @param description
     * @param IsPremade
     */
    public Routine(String name, String description, boolean IsPremade) {

        this.idRoutine = Routine.IDENTITY += 1;
        this.name = name;
        this.description = description;
        this.IsPremade = IsPremade;
    }

    /** Constructor for Parcel.
     * @param parcel
     */
    public Routine(Parcel parcel) {
        idRoutine = parcel.readInt();
        name = parcel.readString();
        description = parcel.readString();
        IsPremade = parcel.readByte() != 0;
    }

    /** Display order for the Routine.
     * @return int
     */
    public int getDisplayOrder() {
        return displayOrder;
    }

    /** Set display order for the Routine.
     * @param displayOrder
     */
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    // For Parcel.
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idRoutine);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeByte((byte) (IsPremade ? 1 : 0));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdRoutine() {
        return idRoutine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // For Parcel.
    @Override
    public int describeContents() {
        return 0;
    }
}
