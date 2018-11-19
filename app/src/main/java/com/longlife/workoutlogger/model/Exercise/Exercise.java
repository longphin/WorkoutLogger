package com.longlife.workoutlogger.model.Exercise;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;
import com.longlife.workoutlogger.enums.MeasurementType;
import com.longlife.workoutlogger.enums.MeasurementTypeConverter;
import com.longlife.workoutlogger.utils.DateConverter;
import com.longlife.workoutlogger.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This will be the Exercise object.
 */

@Entity(
        /*indices = {
        @Index(value = {"locked"}),
        @Index(value = {"hidden"})
}
*/
)
public class Exercise implements Parcelable, JSONParser.JSON {
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
    @Ignore
    private static final String JSON_NAME = "name";
    @Ignore
    private static final String JSON_NOTE = "note";
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
    //@TypeConverters({ExerciseTypeConverter.class})
    //private ExerciseType exerciseType; // The type of exercise, such as weight, bodyweight, distance.
    private int exerciseType;

    public Exercise() {

    }

    @Ignore
    private Exercise(Parcel parcel) {
        idExercise = parcel.readLong();
        name = parcel.readString();
    }
    @TypeConverters({MeasurementTypeConverter.class})
    private MeasurementType measurementType; // The measurement of the exercise, such as reps or duration.
    // That that this instance was created.
    @TypeConverters({DateConverter.class})
    @NonNull
    private Date lastUpdateDate = (new GregorianCalendar()).getTime();
    @Nullable
    private Boolean isPreloaded = false;

    // Constructor that builds an exercise from a JSON object.
    @Ignore
    public Exercise(JSONObject json) {
        this.name = json.optString(Exercise.JSON_NAME, "");
        this.note = json.optString(Exercise.JSON_NOTE, "");

        if (json.has("isPreloaded"))
            this.isPreloaded = json.optBoolean("isPreloaded", false);
        else
            this.isPreloaded = null;

        //this.isPreloaded = json.optBoolean("isPreloaded");
    }

    @Ignore
    public Exercise(String name, String descrip) {
        this.name = name;
        this.note = descrip;
    }

    public int getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(int exerciseType) {
        this.exerciseType = exerciseType;
    }

    @Nullable
    public Boolean getPreloaded() {
        return isPreloaded;
    }

    public void setPreloaded(@Nullable Boolean preloaded) {
        isPreloaded = preloaded;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public ExerciseType getExerciseType() {
        return exerciseType;
    }*/
    /*public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }*/

    public Long getIdExercise() {

        return idExercise;
    }

    public void setIdExercise(Long val) {
        idExercise = val;
    }

    public boolean isLocked() {
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

    @Ignore
    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Exercise.JSON_NAME, name);
            jsonObject.put(Exercise.JSON_NOTE, note);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}


