package com.longlife.workoutlogger.model;

import com.longlife.workoutlogger.enums.ExerciseRequestCode;

/**
 * All Exercises should extend this class.
 */

abstract class ExerciseAbstract {
    // Each Exercise will be given an Id.
    final private int idExercise;
    // For Exercises that are copies of the shared database, this will be the idExercise from the shared database.
    private int idExerciseShared;
    private String name = "new";
    private String description = "";
    // Official Exercises will have this flag as true.
    private boolean IsPremade;

    private ExerciseRequestCode.ExerciseType exerciseType; // The type of exercise, such as weight, bodyweight, distance.
    private ExerciseRequestCode.MeasurementType measurementType; // The measurement of the exercise, such as reps or duration.

    // Normal constructor.
    public ExerciseAbstract(int idExercise, ExerciseRequestCode.ExerciseType et, ExerciseRequestCode.MeasurementType mt, boolean IsPremade) {
        this.idExercise = idExercise;
        this.IsPremade = IsPremade;
        this.exerciseType = et;
        this.measurementType = mt;
    }

    // Default constructor for non-premade Exercises.
    public ExerciseAbstract(int idExercise, ExerciseRequestCode.ExerciseType et, ExerciseRequestCode.MeasurementType mt) {
        this(idExercise, et, mt, false);
    }

    public ExerciseRequestCode.MeasurementType getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(ExerciseRequestCode.MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public ExerciseRequestCode.ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseRequestCode.ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public int getIdExercise() {

        return idExercise;
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
