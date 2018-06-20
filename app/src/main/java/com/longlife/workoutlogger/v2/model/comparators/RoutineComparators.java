package com.longlife.workoutlogger.v2.model.comparators;

import com.longlife.workoutlogger.v2.data.UserProfile;
import com.longlife.workoutlogger.v2.model.Routine;

import java.util.Comparator;

/**
 * Created by Longphi on 2/2/2018.
 */
public class RoutineComparators {
    public static DefaultComparator getDefaultComparator() {
        return new DefaultComparator();
    }

    private static class DefaultComparator implements Comparator<Routine> {
        @Override
        public int compare(Routine r1, Routine r2) {
            // Order by order.
            if (r1.getDisplayOrder() > r2.getDisplayOrder()) return -1;
            else if (r1.getDisplayOrder() < r2.getDisplayOrder()) return 1;

            // Order by name.
            int res = r1.getName().toLowerCase(UserProfile.getLocale()).compareTo(r2.getName().toLowerCase(UserProfile.getLocale()));

            // Order by id.
            if (res == 0) return (r1.getIdRoutine() >= r2.getIdRoutine() ? 1 : -1);
            return (res);
        }
    }
}