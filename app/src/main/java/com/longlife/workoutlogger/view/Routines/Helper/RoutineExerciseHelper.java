package com.longlife.workoutlogger.view.Routines.Helper;

import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.List;

// This is a simple object that contains the exercise and the details for associated sets.
// This is used when creating a routine.
public class RoutineExerciseHelper {
    private ExerciseShort exercise;
    private List<SessionExerciseSet> sets;
    private boolean isExpanded;

    public RoutineExerciseHelper(ExerciseShort ex, List<SessionExerciseSet> sets, boolean isExpanded) {
        this.exercise = ex;
        this.sets = sets;
        this.isExpanded = isExpanded;
    }

    public RoutineExerciseHelper(ExerciseSessionWithSets exerciseWithSets) {
        this.exercise = exerciseWithSets.getExercise();
        this.sets = exerciseWithSets.getSets();
        this.isExpanded = true;
    }

    public ExerciseShort getExercise() {
        return exercise;
    }


    public void setExercise(ExerciseShort exercise) {
        this.exercise = exercise;
    }

    public List<SessionExerciseSet> getSets() {
        return sets;
    }

    public boolean IsExpanded() {
        return isExpanded;
    }

    public void IsExpanded(boolean bool) {
        isExpanded = bool;
    }
}
