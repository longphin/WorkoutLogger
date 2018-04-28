package com.longlife.workoutlogger.v2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

/**
 * Created by Longphi on 1/4/2018.
 */
@Entity(foreignKeys = @ForeignKey(entity = SessionExercise.class, parentColumns = "idSessionExercise", childColumns = "idSessionExercise", onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"idSessionExercise"})}
)
public class SessionExerciseSet {
    @PrimaryKey
    @NonNull
    private int idSessionExerciseSet;
    private int idSessionExercise;
    private int idExercise; // This makes it easier to get the exercise types.

    private Integer reps;
    private Double weights;
    private float rest;
    private float duration;

    public SessionExerciseSet() {

    }

    public int getIdSessionExerciseSet() {
        return idSessionExerciseSet;
    }

    public void setIdSessionExerciseSet(int i) {
        idSessionExerciseSet = i;
    }

    public int getIdExercise() {
        return (this.idExercise);
    }

    public void setIdExercise(int i) {
        idExercise = i;
    }

    public int getIdSessionExercise() {
        return idSessionExercise;
    }

    public void setIdSessionExercise(int i) {
        idSessionExercise = i;
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
