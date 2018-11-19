package com.longlife.workoutlogger.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExerciseType {
    private static final int WEIGHT_AND_REP = 0;
    private static final int BODYWEIGHT = 1;

    public ExerciseType() {

    }

    public static int getDefault() {
        return WEIGHT_AND_REP;
    }

    public static List<Type> getOptions(Locale locale) {
        List<Type> options = new ArrayList<>();

        options.add(getUnit(WEIGHT_AND_REP, locale));
        options.add(getUnit(BODYWEIGHT, locale));

        return options;
    }

    private static Type getUnit(int weightType, Locale locale) {
        if (weightType == WEIGHT_AND_REP) {
            if (locale == Locale.US
                    || locale == Locale.ENGLISH) {
                return new Type(WEIGHT_AND_REP, "Weight + Reps");
            }

            return new Type(WEIGHT_AND_REP, "Weight + Reps");
        }

        if (weightType == BODYWEIGHT) {
            if (locale == Locale.US
                    || locale == Locale.ENGLISH) {
                return new Type(BODYWEIGHT, "Bodyweight");
            }

            return new Type(BODYWEIGHT, "Bodyweight");
        }

        return new Type(-1, "error");
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
