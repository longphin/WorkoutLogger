package com.longlife.workoutlogger.view.Exercises.Helper;

import com.longlife.workoutlogger.model.ExerciseShort;

// A helper class for when an exercise is being deleted. When an exercise is beginning to be deleted,
// it will be stored in this class. This class helps for when the delete is undone or when the delete is finalized.
public class DeletedExercise {
    private ExerciseShort exercise;
    private int position;

    public DeletedExercise(ExerciseShort exercise, int position) {
        this.exercise = exercise;
        this.position = position;
    }


    public ExerciseShort getExercise() {
        return exercise;
    }

    public int getPosition() {
        return position;
    }
}
