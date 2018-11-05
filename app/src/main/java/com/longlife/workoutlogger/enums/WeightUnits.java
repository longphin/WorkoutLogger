package com.longlife.workoutlogger.enums;

import android.content.Context;
import android.support.annotation.NonNull;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.data.getLocaleResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeightUnits {
    private List<Unit> units = new ArrayList<>();

    public WeightUnits(Context context, Locale locale) {
        String[] unitsList = (new getLocaleResource()).getLocalizedResources(context, locale).getStringArray(R.array.units_array);

        for (int i = 0; i < unitsList.length; i++) {
            units.add(new Unit(i, unitsList[i]));
        }
    }

    @NonNull
    public List<Unit> getUnitsList() {
        return units;
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
