/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/23/18 5:57 PM.
 */

package com.longlife.workoutlogger.enums;

import android.content.Context;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.GetResource;

import java.util.ArrayList;
import java.util.List;

public class WeightUnitTypes {
    private static final int LBS = 0;
    private static final int KGS = 1;

    public WeightUnitTypes() {

    }

    public static int getDefault() {
        return LBS;
    }

    public static List<Unit> getOptions(Context context) {
        List<Unit> options = new ArrayList<>();

        options.add(new Unit(LBS, GetResource.getStringResource(context, R.string.WEIGHTUNIT_lbs)));
        options.add(new Unit(KGS, GetResource.getStringResource(context, R.string.WEIGHTUNIT_kbs)));

        return options;
    }

    public static class Unit {
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
