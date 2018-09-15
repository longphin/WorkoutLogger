package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
        @Index(value = {"locked", "name"}),
        @Index(value = {"idExerciseSource", "hidden", "name"})
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

    @Required
    private String name;
    @PrimaryKey
    private Long idExercise;
    // This is the idExercise for the parent exercise.
    @Nullable
    private Long idExerciseSource;
    // This is the idExercise for the leaf-most child. This is only relevant for source idExercises.
    @Nullable
    private Long idExerciseLeaf;
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
    private Date createDate = (new GregorianCalendar()).getTime();

    // Copy constructor. Does not copy idExercise.
    @Ignore
    public Exercise(Exercise ex) {
        name = ex.getName();
        idExerciseSource = (ex.getIdExerciseSource() == null ? ex.getIdExercise() : ex.getIdExerciseSource());
        idExerciseLeaf = ex.getIdExerciseLeaf(); // Leaf nodes do not need this, but this is done for completeness.
        note = ex.getNote();
        locked = ex.getLocked(); // This is not used by leaf nodes.
        hidden = ex.isHidden(); // This is not used by leaf nodes.
        exerciseType = ex.getExerciseType();
        measurementType = ex.getMeasurementType();
        createDate = (new GregorianCalendar()).getTime();
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

    @Nullable
    public Long getIdExerciseSource() {
        return idExerciseSource;
    }

    public void setIdExerciseSource(@Nullable Long idExerciseSource) {
        this.idExerciseSource = idExerciseSource;
    }

    @Nullable
    public Long getIdExerciseLeaf() {
        return idExerciseLeaf;
    }

    public void setIdExerciseLeaf(@Nullable Long idExerciseLeaf) {
        this.idExerciseLeaf = idExerciseLeaf;
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
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(@NonNull Date createDate) {
        this.createDate = createDate;
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

    public void setCreateDateAsNow() {
        createDate = (new GregorianCalendar()).getTime();
    }
}


