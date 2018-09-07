package com.longlife.workoutlogger.model.comparators;

import com.longlife.workoutlogger.model.Routine;

import java.util.Comparator;

/**
 * Created by Longphi on 2/2/2018.
 */

public class RoutineComparator
        implements Comparator<Routine> {

    @Override
    public int compare(Routine r1, Routine r2) {
        // Order by name.
        int res = r1.getName().compareTo(r2.getName());

        // Order by id.
        if (res == 0)
            return (r1.getIdRoutine() >= r2.getIdRoutine() ? 1 : -1);
        return (res);
    }
}


