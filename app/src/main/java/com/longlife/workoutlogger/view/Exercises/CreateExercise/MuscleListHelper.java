/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/22/18 10:16 PM.
 */

package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import com.longlife.workoutlogger.enums.Muscle;

import java.util.List;

public class MuscleListHelper {
    private int idMuscleGroup;
    private String muscleGroupName;
    private List<Muscle> muscles;
    private int headerPadding;
    private int footerPadding;
    private int visiblePosition;
    private boolean isExpanded = true;

    public MuscleListHelper(int idMuscleGroup, String muscleGroupName, List<Muscle> muscles, int numberOfColumns) {
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

    int getHeaderPadding() {
        return headerPadding;
    }

    public void setHeaderPadding(int headerPadding) {
        this.headerPadding = headerPadding;
    }

    int getVisiblePosition() {
        return visiblePosition;
    }

    void setVisiblePosition(int visiblePosition) {
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

    String getMuscleGroupName() {
        return muscleGroupName;
    }

    public List<Muscle> getMuscles() {
        return muscles;
    }

    int getFooterPadding() {
        return footerPadding;
    }
}
