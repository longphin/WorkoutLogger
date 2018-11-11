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

    /*
         // Each element is the visible position of the header item. headerItemPosition.size() is the same as exercisesToInclude.size()
         // Example:
         1 Header1 (expanded)
         2 Set1
         3 Set2
         4 Header2 (collapsed, but has 2 sets)
         5 Header3 (expanded)
         6 Set1

         -> headerItemPosition = [1, 4, 5]
        */
    private int headerPosition; // Position of the header item within the visible adapter items.

    public RoutineExerciseHelper(ExerciseShort ex, List<SessionExerciseSet> sets, boolean isExpanded, int headerPosition) {
        this.exercise = ex;
        this.sets = sets;
        this.isExpanded = isExpanded;
        this.headerPosition = headerPosition;
    }

    public RoutineExerciseHelper(ExerciseSessionWithSets exerciseWithSets, int headerPosition) {
        this.exercise = exerciseWithSets.getExercise();
        this.sets = exerciseWithSets.getSets();
        this.isExpanded = true;
        this.headerPosition = headerPosition;
    }

    public int getHeaderPosition() {
        return headerPosition;
    }

    public void setHeaderPosition(int headerPosition) {
        this.headerPosition = headerPosition;
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
