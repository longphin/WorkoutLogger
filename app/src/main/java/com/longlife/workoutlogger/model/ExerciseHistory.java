package com.longlife.workoutlogger.model;

import android.arch.persistence.room.*;
import io.reactivex.annotations.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Exercise.class, parentColumns = "idExercise", childColumns = "idExercise", onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"idExercise"})})
public class ExerciseHistory {
    @PrimaryKey
    @NonNull
    private Long idExerciseHistory;
    @NonNull
    private Long idExercise;
    @NonNull
    private String name;
    private String note;

    public ExerciseHistory() {
    }

    @Ignore
    public ExerciseHistory(Exercise ex) {
        this.idExercise = ex.getIdExercise();
        this.name = ex.getName();
    }


    public Long getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
    }

    public Long getIdExerciseHistory() {
        return idExerciseHistory;
    }


    public void setIdExerciseHistory(Long idExerciseHistory) {
        this.idExerciseHistory = idExerciseHistory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {

        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
