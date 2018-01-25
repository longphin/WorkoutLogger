package com.longlife.workoutlogger.model;

/**
 * Created by Longphi on 1/4/2018.
 */

public class SessionExerciseSet {
    private static int IDENTITY = 0;
    private final int idSessionExerciseSet;
    private final int idSessionExercise;
    private int displayOrder;
    private int reps;
    private int weights;
    private float rest;
    private float duration;

    public SessionExerciseSet(SessionExercise sessionExercise)
    {
        this.idSessionExerciseSet = IDENTITY += 1;
        this.idSessionExercise = sessionExercise.getIdSessionExercise();
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
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

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeights() {
        return weights;
    }

    public void setWeights(int weights) {
        this.weights = weights;
    }

    public float getRest() {
        return rest;
    }

    public void setRest(float rest) {
        this.rest = rest;
    }
}
