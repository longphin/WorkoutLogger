package com.longlife.workoutlogger.utils;

import com.longlife.workoutlogger.model.Exercise;

import java.util.Comparator;

/**
 * Created by Longphi on 2/2/2018.
 */

public class ExerciseComparator implements Comparator<Exercise> {

    @Override
    public int compare(Exercise e1, Exercise e2) {
        // Order by name.
        int res = e1.getName().compareTo(e2.getName());

        // Order by id.
        if (res == 0) return (e1.getIdExercise() >= e2.getIdExercise() ? 1 : -1);
        return (res);
    }
}
