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

        options.add(new Type(WEIGHT_AND_REP, GetResource.getStringResource(context, R.string.EXERCISETYPE_weight_and_rep)));
        options.add(new Type(REPS, GetResource.getStringResource(context, R.string.EXERCISETYPE_reps)));
        options.add(new Type(DISTANCE_AND_TIME, GetResource.getStringResource(context, R.string.EXERCISETYPE_distance_and_time)));
        options.add(new Type(DURATION, GetResource.getStringResource(context, R.string.EXERCISETYPE_duration)));
        options.add(new Type(COUNTDOWN, GetResource.getStringResource(context, R.string.EXERCISETYPE_countdown)));

        return options;
    }

    public static class Type {
        int id;
        String label;

        private Type(int id, String label) {
            this.id = id;
            this.label = label;
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
