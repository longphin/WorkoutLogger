/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/8/18 11:01 AM.
 */

package com.longlife.workoutlogger.enums;

import android.content.Context;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.GetResource;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListGroupBy {
    private static final int GROUP_BY_NAME = 0;
    private static final int GROUP_BY_MUSCLE_GROUP_CHEST = 1;
    private static final int GROUP_BY_MUSCLE_GROUP_BACK = 2;
    private static final int GROUP_BY_MUSCLE_GROUP_ARMS = 3;
    private static final int GROUP_BY_MUSCLE_GROUP_SHOULDERS = 4;
    private static final int GROUP_BY_MUSCLE_GROUP_LEGS = 5;
    private static final int GROUP_BY_MUSCLE_GROUP_CORE = 6;
    private static final int GROUP_BY_MUSCLE_GROUP_MISC = 7;

    public static List<Type> getOptions(Context context) {
        String prefix = GetResource.getStringResource(context, R.string.GROUPBY_PREFIX);

        List<Type> options = new ArrayList<>();
        options.add(new Type(GROUP_BY_NAME, GetResource.getStringResource(context, R.string.GROUPBY_NAME)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_MISC, prefix + " " + MuscleGroup.getMuscleGroupName(context, R.integer.MUSCLEGROUP_misc)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_ARMS, prefix + " " + MuscleGroup.getMuscleGroupName(context, R.integer.MUSCLEGROUP_arms)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_BACK, prefix + " " + MuscleGroup.getMuscleGroupName(context, R.integer.MUSCLEGROUP_back)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_CHEST, prefix + " " + MuscleGroup.getMuscleGroupName(context, R.integer.MUSCLEGROUP_chest)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_CORE, prefix + " " + MuscleGroup.getMuscleGroupName(context, R.integer.MUSCLEGROUP_core)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_LEGS, prefix + " " + MuscleGroup.getMuscleGroupName(context, R.integer.MUSCLEGROUP_legs)));
        options.add(new Type(GROUP_BY_MUSCLE_GROUP_SHOULDERS, prefix + " " + MuscleGroup.getMuscleGroupName(context, R.integer.MUSCLEGROUP_shoulders)));

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

        @Override
        public String toString() {
            return label;
        }
    }
}
