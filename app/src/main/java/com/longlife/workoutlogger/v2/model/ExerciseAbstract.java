package com.longlife.workoutlogger.v2.model;

import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.longlife.workoutlogger.enums.ExerciseType;
import com.longlife.workoutlogger.enums.ExerciseTypeConverter;
import com.longlife.workoutlogger.enums.MeasurementType;
import com.longlife.workoutlogger.enums.MeasurementTypeConverter;

import io.reactivex.annotations.NonNull;

/**
 * All Exercises should extend this class.
 */
abstract class ExerciseAbstract {
    // Each Exercise will be given an Id.
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int idExercise;
    // For Exercises that are copies of the shared database, this will be the idExercise from the shared database.
    private int idExerciseShared;
    private String name = "new";
    private String description = "";
    // Official Exercises will have this flag as true.
    private boolean IsPremade;

    @TypeConverters({ExerciseTypeConverter.class})
    private ExerciseType exerciseType; // The type of exercise, such as weight, bodyweight, distance.
    @TypeConverters({MeasurementTypeConverter.class})
    private MeasurementType measurementType; // The measurement of the exercise, such as reps or duration.

    public ExerciseAbstract() {

    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public int getIdExercise() {

        return idExercise;
    }

    public void setIdExercise(int val) {
        idExercise = val;
    }

    public int getIdExerciseShared() {
        return idExerciseShared;
    }

    public void setIdExerciseShared(int idExerciseShared) {
        this.idExerciseShared = idExerciseShared;
    }

    public String getName() {
        return name;
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

    public boolean getIsPremade() {
        return IsPremade;
    }

    public void setIsPremade(boolean premade) {
        IsPremade = premade;
    }
}
