package com.longlife.workoutlogger.model;

import android.arch.persistence.room.*;
import android.os.Parcel;
import android.os.Parcelable;
import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;
import com.longlife.workoutlogger.enums.ExerciseType;
import com.longlife.workoutlogger.enums.ExerciseTypeConverter;
import com.longlife.workoutlogger.enums.MeasurementType;
import com.longlife.workoutlogger.enums.MeasurementTypeConverter;
import io.reactivex.annotations.NonNull;

/**
 * This will be the Exercise object.
 */

@Entity(indices = {
        @Index(value = {"locked", "name"}),
        @Index(value = {"hidden", "name"})
}
)
public class Exercise
        implements Parcelable {
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
    // Name for exercise.
    @Required
    private String name;
    @PrimaryKey
    @NonNull
    private Long idExercise;
    // This is the idExerciseHistory that this current exercise corresponds to.
    private Long currentIdExerciseHistory;
    // Note for the exercise.
    private String note;
    // Flag to indicate whether exercise is locked.
    private boolean locked;
    // Flag to indicate whether exercise is hidden.
    @NonNull
    private boolean hidden = false;
    // Type of exercise, used to determine how the exercise should be recorded.
    @TypeConverters({ExerciseTypeConverter.class})
    private ExerciseType exerciseType; // The type of exercise, such as weight, bodyweight, distance.
    @TypeConverters({MeasurementTypeConverter.class})
    private MeasurementType measurementType; // The measurement of the exercise, such as reps or duration.

    public Exercise() {

    }

    public Exercise(String name, String descrip) {
        this.name = name;
        this.note = descrip;
    }

    @Ignore
    private Exercise(Parcel parcel) {
        idExercise = parcel.readLong();
        name = parcel.readString();
    }


    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public Long getCurrentIdExerciseHistory() {
        return currentIdExerciseHistory;
    }


    public void setCurrentIdExerciseHistory(Long currentIdExerciseHistory) {
        this.currentIdExerciseHistory = currentIdExerciseHistory;
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
}


