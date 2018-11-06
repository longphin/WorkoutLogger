package com.longlife.workoutlogger.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeightUnitTypes {
    private static final int LBS = 0;
    private static final int KGS = 1;

    public WeightUnitTypes() {

    }

    public static int getDefault() {
        return LBS;
    }

    public List<Unit> getWeightOptions(Locale locale) {
        List<Unit> options = new ArrayList<>();

        options.add(getUnit(LBS, locale));
        options.add(getUnit(KGS, locale));

        return options;
    }

    private Unit getUnit(int weightType, Locale locale) {
        if (weightType == LBS) {
            if (locale == Locale.US
                    || locale == Locale.ENGLISH) {
                return new Unit(LBS, "lbs");
            }

            return new Unit(LBS, "lbs");
        }

        if (weightType == KGS) {
            if (locale == Locale.US
                    || locale == Locale.ENGLISH) {
                return new Unit(KGS, "kgs");
            }

            return new Unit(KGS, "kgs");
        }

        return new Unit(-1, "error");
    }

    public class Unit {
        int id;
        String label;

        private Unit(int id, String label) {
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
