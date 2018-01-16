package com.longlife.workoutlogger.model;

/**
 * Created by Longphi on 1/4/2018.
 */

public class SessionExerciseSet {
    private final int idSessionExercise;
    private int reps;
    private int weights;
    private float rest;

    public SessionExerciseSet(int idSessionExercise)
    {
        this.idSessionExercise = idSessionExercise;
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
