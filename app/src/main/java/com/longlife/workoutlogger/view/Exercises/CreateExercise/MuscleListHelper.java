package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import com.longlife.workoutlogger.enums.MuscleClass;

import java.util.List;

public class MuscleListHelper {
    private int idMuscleGroup;
    private String muscleGroupName;
    private List<MuscleClass> muscles;
    private int headerPadding;
    private int footerPadding;
    private int visiblePosition;
    private boolean isExpanded = true;

    public MuscleListHelper(int idMuscleGroup, String muscleGroupName, List<MuscleClass> muscles, int numberOfColumns) {
        this.idMuscleGroup = idMuscleGroup;
        this.muscleGroupName = muscleGroupName;
        this.muscles = muscles;
        calculateHeaderPadding();
        calculateFooterPadding(numberOfColumns);
    }

    private void calculateHeaderPadding() {
        headerPadding = MuscleListAdapter.NUMBER_OF_COLUMNS - 1;
    }

    private void calculateFooterPadding(int numberOfColumns) {
        footerPadding = muscles.size() % numberOfColumns;
    }

    public int getHeaderPadding() {
        return headerPadding;
    }

    public void setHeaderPadding(int headerPadding) {
        this.headerPadding = headerPadding;
    }

    public int getVisiblePosition() {
        return visiblePosition;
    }

    public void setVisiblePosition(int visiblePosition) {
        this.visiblePosition = visiblePosition;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getIdMuscleGroup() {
        return idMuscleGroup;
    }

    public String getMuscleGroupName() {
        return muscleGroupName;
    }

    public List<MuscleClass> getMuscles() {
        return muscles;
    }

    public int getFooterPadding() {
        return footerPadding;
    }
}
