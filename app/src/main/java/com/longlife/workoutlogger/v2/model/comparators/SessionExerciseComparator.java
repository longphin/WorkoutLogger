package com.longlife.workoutlogger.v2.model.comparators;

import com.longlife.workoutlogger.v2.model.SessionExercise;

import java.util.Comparator;

/**
 * Created by Longphi on 2/2/2018.
 */

public class SessionExerciseComparator implements Comparator<SessionExercise> {
    @Override
    public int compare(SessionExercise se1, SessionExercise se2) {
        // Order by display order
        if (se1.getDisplayOrder() > se2.getDisplayOrder()) return (1);
        else if (se1.getDisplayOrder() < se2.getDisplayOrder()) return (-1);

        // Order by id.
        return (se1.getIdSessionExercise() >= se2.getIdSessionExercise() ? 1 : -1);
    }
}
