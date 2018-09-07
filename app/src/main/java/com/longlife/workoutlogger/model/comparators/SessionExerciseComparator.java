package com.longlife.workoutlogger.model.comparators;

import com.longlife.workoutlogger.model.SessionExercise;

import java.util.Comparator;

/**
 * Created by Longphi on 2/2/2018.
 */

public class SessionExerciseComparator
        implements Comparator<SessionExercise> {

    @Override
    public int compare(SessionExercise se1, SessionExercise se2) {
        // Order by id.
        if (se1.getIdSessionExercise().equals(se2.getIdSessionExercise())) return 0;
        return (se1.getIdSessionExercise() >= se2.getIdSessionExercise() ? 1 : -1);
    }
}


