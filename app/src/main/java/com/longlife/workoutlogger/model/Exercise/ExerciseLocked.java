package com.longlife.workoutlogger.model.Exercise;

// Helper class for when an exercise's lock status is changed.
public class ExerciseLocked {
    private Long idExercise;
    private boolean isLocked;

    public ExerciseLocked(Long idExercise, boolean isLocked) {
        this.idExercise = idExercise;
        this.isLocked = isLocked;
    }


    public Long getIdExercise() {
        return idExercise;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
