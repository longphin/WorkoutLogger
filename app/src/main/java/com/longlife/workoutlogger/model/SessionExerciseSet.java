package com.longlife.workoutlogger.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Longphi on 1/4/2018.
 */
@Entity(foreignKeys = @ForeignKey(entity = SessionExercise.class, parentColumns = "idSessionExercise", childColumns = "idSessionExercise", onDelete = ForeignKey.CASCADE),
    indices = {@Index(value = {"idSessionExercise"})}
)
public class SessionExerciseSet {
    @Ignore
    private static int IDENTITY = 0;
    @PrimaryKey
    private int idSessionExerciseSet;
    private int idSessionExercise;
    private int idExercise; // This makes it easier to get the exercises types.

    private Integer reps;
    private Double weights;
    private float rest;
    private float duration;

    public SessionExerciseSet() {
        idSessionExerciseSet = IDENTITY += 1;
    }

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

    public void setIdSessionExerciseSet(int i) {
        idSessionExerciseSet = i;
    }

    public void setIdSessionExercise(int i) {
        idSessionExercise = i;
    }

    public void setIdExercise(int i) {
        idExercise = i;
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
