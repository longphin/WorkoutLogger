package com.longlife.workoutlogger.model;

/**
 * Created by Longphi on 1/4/2018.
 */

abstract class ExerciseAbstract {
    final private int idExercise;
    private int idExerciseShared; // this will be the idExercise for an exercise in the official database
    private String name = "new";
    private String description = "";
    private boolean IsPremade;

    public ExerciseAbstract(int idExercise, boolean IsPremade)
    {
        this.idExercise = idExercise;
        this.IsPremade = IsPremade;
    }

    public ExerciseAbstract(int idExercise)
    {
        this(idExercise, false);
    }

    public void setIsPremade(boolean premade) {
        IsPremade = premade;
    }

    public void setIdExerciseShared(int idExerciseShared) {
        this.idExerciseShared = idExerciseShared;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdExercise() {

        return idExercise;
    }

    public int getIdExerciseShared() {
        return idExerciseShared;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsPremade() {
        return IsPremade;
    }
}
