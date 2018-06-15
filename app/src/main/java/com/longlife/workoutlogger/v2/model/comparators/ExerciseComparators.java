package com.longlife.workoutlogger.v2.model.comparators;

import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Longphi on 2/2/2018.
 */
public class ExerciseComparators {
    public static DefaultComparator getDefaultComparator() {
        return new DefaultComparator();
    }

    private static class DefaultComparator implements Comparator<Exercise> {
        @Override
        public int compare(Exercise e1, Exercise e2) {
            // Order by favorites.
            if ((e1.getFavorited() ? 1 : 0) > (e2.getFavorited() ? 1 : 0)) return -1;
            else if ((e1.getFavorited() ? 1 : 0) < (e2.getFavorited() ? 1 : 0)) return 1;

            // Order by name.
            int res = e1.getName().toLowerCase(Locale.US).compareTo(e2.getName().toLowerCase(Locale.US)); //[TODO] Make country a user setting.

            // Order by id.
            if (res == 0) return (e1.getIdExercise() >= e2.getIdExercise() ? 1 : -1);
            return (res);
        }
    }
}
