package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;
import com.longlife.workoutlogger.enums.ExerciseType;
import com.longlife.workoutlogger.enums.ExerciseTypeConverter;
import com.longlife.workoutlogger.enums.MeasurementType;
import com.longlife.workoutlogger.enums.MeasurementTypeConverter;
import com.longlife.workoutlogger.utils.DateConverter;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This will be the Exercise object.
 */

@Entity(indices = {
        @Index(value = {"locked"}),
        @Index(value = {"hidden"})
}
)
public class Exercise implements Parcelable {
    @Ignore
    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {


        @Override
        public Exercise createFromParcel(Parcel parcel) {
            return new Exercise(parcel);
        }

        @Override
        public Exercise[] newArray(int i) {
            return new Exercise[i];
        }
    };

    // Name of the exercise.
    @Required
    private String name;
    // This is the idExercise for the exercise.
    @PrimaryKey
    private Long idExercise;

    // Note for the exercise.
    private String note;
    // Flag to indicate whether exercise is locked.
    private boolean locked;
    // Flag to indicate whether exercise is hidden.
    private boolean hidden = false;
    // Type of exercise, used to determine how the exercise should be recorded.
    @TypeConverters({ExerciseTypeConverter.class})
    private ExerciseType exerciseType; // The type of exercise, such as weight, bodyweight, distance.
    @TypeConverters({MeasurementTypeConverter.class})
    private MeasurementType measurementType; // The measurement of the exercise, such as reps or duration.
    // That that this instance was created.
    @TypeConverters({DateConverter.class})
    @NonNull
    private Date lastUpdateDate = (new GregorianCalendar()).getTime();

    // Copy constructor. Does not copy idExercise.
    @Ignore
    public Exercise(Exercise ex) {
        name = ex.getName();
        note = ex.getNote();
        locked = ex.getLocked(); // This is not used by leaf nodes.
        hidden = ex.isHidden(); // This is not used by leaf nodes.
        exerciseType = ex.getExerciseType();
        measurementType = ex.getMeasurementType();
        lastUpdateDate = (new GregorianCalendar()).getTime();
    }

    public Exercise() {

    }

    public String getName() {
        return name;
    }

    @Ignore
    private Exercise(Parcel parcel) {
        idExercise = parcel.readLong();
        name = parcel.readString();
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(idExercise);
        parcel.writeString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Ignore
    public Exercise(String name, String descrip) {
        this.name = name;
        this.note = descrip;
    }

    @NonNull
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(@NonNull Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString() {
        return getName();
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public Long getIdExercise() {

        return idExercise;
    }

    public void setIdExercise(Long val) {
        idExercise = val;
    }

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean b) {
        hidden = b;
    }

    public void setUpdateDateAsNow() {
        lastUpdateDate = (new GregorianCalendar()).getTime();
    }
}


