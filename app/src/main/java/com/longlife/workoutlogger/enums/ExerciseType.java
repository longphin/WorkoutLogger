package com.longlife.workoutlogger.enums;

import android.content.Context;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.GetResource;

import java.util.ArrayList;
import java.util.List;

public class ExerciseType {
    private static final int WEIGHT_AND_REP = 0;
    private static final int REPS = 1;
    private static final int DISTANCE_AND_TIME = 2;
    private static final int DURATION = 3;
    private static final int COUNTDOWN = 4;

    public ExerciseType() {

    }

    public static List<Type> getOptions(Context context) {
        List<Type> options = new ArrayList<>();

        options.add(new Type(context, WEIGHT_AND_REP));
        options.add(new Type(context, REPS));
        options.add(new Type(context, DISTANCE_AND_TIME));
        options.add(new Type(context, DURATION));
        options.add(new Type(context, COUNTDOWN));

        return options;
    }

    public static List<Type> getOptionsListWithOneItemOnly(Context context, int idExerciseType) {
        List<Type> res = new ArrayList<>();
        res.add(new Type(context, idExerciseType));
        return res;
    }

    public static class Type {
        int id;
        String label;

        private Type(Context context, int id) {
            this.id = id;
            this.label = getName(context, id);
        }

        private String getName(Context context, int id) {
            switch (id) {
                case WEIGHT_AND_REP:
                    return GetResource.getStringResource(context, R.string.EXERCISETYPE_weight_and_rep);
                case REPS:
                    return GetResource.getStringResource(context, R.string.EXERCISETYPE_reps);
                case DISTANCE_AND_TIME:
                    return GetResource.getStringResource(context, R.string.EXERCISETYPE_distance_and_time);
                case DURATION:
                    return GetResource.getStringResource(context, R.string.EXERCISETYPE_duration);
                case COUNTDOWN:
                    return GetResource.getStringResource(context, R.string.EXERCISETYPE_countdown);
                default:
                    return "Exercise type is not named.";
            }
        }

        public int getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
