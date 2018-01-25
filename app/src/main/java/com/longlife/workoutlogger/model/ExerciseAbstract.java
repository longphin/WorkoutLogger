package com.longlife.workoutlogger.model;

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

    // Normal constructor.
    public ExerciseAbstract(int idExercise, boolean IsPremade) {
        this.idExercise = idExercise;
        this.IsPremade = IsPremade;
    }

    // Default constructor for non-premade Exercises.
    public ExerciseAbstract(int idExercise) {
        this(idExercise, false);
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
