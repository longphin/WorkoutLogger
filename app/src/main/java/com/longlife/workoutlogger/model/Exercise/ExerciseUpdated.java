package com.longlife.workoutlogger.model.Exercise;

import android.arch.persistence.room.Relation;

import com.longlife.workoutlogger.model.ExerciseMuscle;

import java.util.List;

public class ExerciseUpdated {
    private Long idExercise;
    private String name;
    private String note;

    @Relation(parentColumn = "idExercise", entityColumn = "idExercise", entity = ExerciseMuscle.class)
    private List<ExerciseMuscle> muscles;

    public List<ExerciseMuscle> getMuscles() {
        return muscles;
    }

    public void setMuscles(List<ExerciseMuscle> muscles) {
        this.muscles = muscles;
    }

    public Long getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
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
