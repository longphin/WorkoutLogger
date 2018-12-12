package com.longlife.workoutlogger.model.Exercise;

public interface IExerciseListable {
    String getName();

    Long getIdExercise();

    void update(ExerciseUpdated updatedExercise);

    void setLocked(boolean locked);

    String getCategory();
}
