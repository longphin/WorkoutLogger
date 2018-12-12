package com.longlife.workoutlogger.model.Exercise;

import android.arch.persistence.room.Ignore;

public class ExerciseWithMuscleGroup implements IExerciseListable {
    public Long idExercise;
    public String name;
    public String note;
    private boolean locked; // Flag to indicate whether exercise is locked.
    private int idMuscleGroup;

    @Ignore
    private String muscleGroupName;

    public ExerciseWithMuscleGroup(Long idExercise, String name, String note, boolean locked, int idMuscleGroup) {
        this.idExercise = idExercise;
        this.name = name;
        this.note = note;
        this.locked = locked;
        this.idMuscleGroup = idMuscleGroup;
    }

    @Ignore
    public void setMuscleGroupName(String muscleGroupName) {
        this.muscleGroupName = muscleGroupName;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
    }

    @Override
    public Long getIdExercise() {
        return idExercise;
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

    public boolean isLocked() {
        return locked;
    }

    @Override
    @Ignore
    public void update(ExerciseUpdated updatedExercise) {
        this.idExercise = updatedExercise.getIdExercise();
        this.name = updatedExercise.getName();
        this.note = updatedExercise.getNote();
    }

    public int getIdMuscleGroup() {
        return idMuscleGroup;
    }

    public void setIdMuscleGroup(int idMuscleGroup) {
        this.idMuscleGroup = idMuscleGroup;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    @Ignore
    public String getCategory() {
        return muscleGroupName;
    }
}
