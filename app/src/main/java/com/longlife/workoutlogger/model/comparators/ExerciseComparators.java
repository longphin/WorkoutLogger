package com.longlife.workoutlogger.model.comparators;

import com.longlife.workoutlogger.model.Exercise;

import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Longphi on 2/2/2018.
 */
public class ExerciseComparators {

    public static DefaultComparator getDefaultComparator() {
        return new DefaultComparator();
    }

    private static class DefaultComparator
            implements Comparator<Exercise> {

        @Override
        public int compare(Exercise e1, Exercise e2) {
            // Order by favorites.
            if ((e1.isLocked() ? 1 : 0) > (e2.isLocked() ? 1 : 0))
                return -1;
            else if ((e1.isLocked() ? 1 : 0) < (e2.isLocked() ? 1 : 0))
                return 1;

            // Order by name.
            Locale locale = Locale.US; // [TODO] this should be obtained from user profile
            int res = e1.getName().toLowerCase(locale).compareTo(e2.getName().toLowerCase(locale));

            // Order by id.
            if (res == 0) {
                if (e1.getIdExercise().equals(e2.getIdExercise())) return 0;
                return (e1.getIdExercise() > e2.getIdExercise() ? 1 : -1);
            }
            return (res);
        }
    }
}


