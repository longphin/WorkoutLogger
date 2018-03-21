package com.longlife.workoutlogger.model;
/**
 * Created by Longphi on 1/4/2018.
 */
public class SessionExerciseSet {
    private static int IDENTITY = 0;
    private final int idSessionExerciseSet;
    private final int idSessionExercise;
    private final int idExercise; // This makes it easier to get the exercise types.

    private Integer reps;
    private Double weights;
    private float rest;
    private float duration;
    public SessionExerciseSet(SessionExercise sessionExercise)
    {
        this.idSessionExerciseSet = IDENTITY += 1;
        this.idExercise = sessionExercise.getIdExercise();
        this.idSessionExercise = sessionExercise.getIdSessionExercise();
    }

    public SessionExerciseSet(SessionExerciseSet sessionExerciseSetToCopy) {
        this.idSessionExerciseSet = IDENTITY += 1;
        this.idExercise = sessionExerciseSetToCopy.getIdExercise();
        this.idSessionExercise = sessionExerciseSetToCopy.getIdSessionExercise();
    }

    public int getIdSessionExerciseSet() {
        return idSessionExerciseSet;
    }

    public int getIdExercise() {
        return (this.idExercise);
    }

    public int getIdSessionExercise() {
        return idSessionExercise;
    }
    public float getDuration() {
        return duration;
    }
    public void setDuration(float duration) {
        this.duration = duration;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Double getWeights() {
        return weights;
    }

    public void setWeights(Double weights) {
        this.weights = weights;
    }
    public float getRest() {
        return rest;
    }
    public void setRest(float rest) {
        this.rest = rest;
    }
}
