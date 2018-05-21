package com.longlife.workoutlogger.v2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

/**
 * Created by Longphi on 1/4/2018.
 */

@Entity(
        foreignKeys = {
                @ForeignKey(entity = RoutineSession.class, parentColumns = "idRoutineSession", childColumns = "idRoutineSession", onDelete = ForeignKey.CASCADE),
                // [TODO] This cascade is not preferred. Rather than deleting exercises, we should maybe hide them. Only delete exercises when they have no references.
                @ForeignKey(entity = Exercise.class, parentColumns = "idExercise", childColumns = "idExercise", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {"idRoutineSession", "idExercise"}),
                @Index(value = {"idRoutineSession"}),
                @Index(value = {"idExercise"})
        }
)
public class SessionExercise {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int idSessionExercise;
    private int idRoutineSession;
    private int idExercise;
    private int displayOrder;
    private int defaultNumberOfSets; // [TODO] this will be updated to be the number of sets in the previous session
    private long defaultRestTime; // [TODO] this will be used to give a default rest time

    public SessionExercise() {

    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getDefaultNumberOfSets() {
        return defaultNumberOfSets;
    }

    public void setDefaultNumberOfSets(int defaultNumberOfSets) {
        this.defaultNumberOfSets = defaultNumberOfSets;
    }

    public long getDefaultRestTime() {
        return defaultRestTime;
    }

    public void setDefaultRestTime(long defaultRestTime) {
        this.defaultRestTime = defaultRestTime;
    }

    public int getIdRoutineSession() {
        return idRoutineSession;
    }

    public void setIdRoutineSession(int i) {
        idRoutineSession = i;
    }

    public int getIdSessionExercise() {
        return idSessionExercise;
    }

    public void setIdSessionExercise(int i) {
        idSessionExercise = i;
    }

    public int getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(int i) {
        idExercise = i;
    }
}